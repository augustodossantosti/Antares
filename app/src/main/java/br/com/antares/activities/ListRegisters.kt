package br.com.antares.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import br.com.antares.R
import br.com.antares.domain.lessons.Lesson
import br.com.antares.domain.register.Register
import br.com.antares.domain.register.Type
import br.com.antares.extensions.onClick
import br.com.antares.infra.CourseService
import br.com.antares.infra.RegisterAdapter
import kotlinx.android.synthetic.main.activity_list_registers.*


/**
 * Activity responsável pela apresentação dos registros (foto, audio, etc)
 * salvos pelo usuário.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class ListRegisters : BaseActivity() {

    private val courseService = CourseService(this)
    private val registerAdapter = RegisterAdapter(this)
    private val registers = ArrayList<Register>()
    private var lesson: Lesson? = null
    private var currentPhotoPath: Uri? = null

    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_registers)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_registers_title)

        recycler_register.setHasFixedSize(false)
        recycler_register.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recycler_register.adapter = registerAdapter

        val lesson = intent.extras.getParcelable<Lesson>(Lesson.LESSON)
        registers.addAll(savedInstanceState?.getParcelableArrayList(Register.REGISTER_LIST)
                ?: (lesson.registers?.toList() ?: emptyList()))
        this.lesson = lesson
        updateList(registers)

        onClick(R.id.new_register) {
            currentPhotoPath = takeAPhoto()
        }

        mShortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {

            val register = Register(type = Type.PHOTO, filepath = currentPhotoPath.toString(), lesson = lesson)
            courseService.createOrUpdateRegister(register)
            registers.add(register)
            updateList(registers)

        } else {
            registers.clear()
            registers.addAll(courseService.listAllRegister(lesson?.id))
            updateList(registers)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelableArrayList(Register.REGISTER_LIST, registers)
        super.onSaveInstanceState(outState)
    }

    private fun updateList(lessons: List<Register>) {
        registerAdapter.updateLessons(lessons)
    }

    fun zoomImageFromThumb(thumbView: View, imageUri: Uri) {

        mCurrentAnimator?.cancel()

        val expandedImageView = findViewById<ImageView>(R.id.expanded_image)
        expandedImageView.setImageURI(imageUri)

        val startBounds = Rect()
        val finalBounds = Rect()
        val globalOffset = Point()

        thumbView.getGlobalVisibleRect(startBounds)
        findViewById<View>(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset)
        startBounds.offset(-globalOffset.x, -globalOffset.y)
        finalBounds.offset(-globalOffset.x, -globalOffset.y)

        val startScale: Float
        if ((finalBounds.width().toFloat()).div(finalBounds.height()) > (startBounds.width().toFloat()).div(startBounds.height())) {
            startScale = startBounds.height().toFloat() / finalBounds.height()
            val startWidth = startScale * finalBounds.width()
            val deltaWidth = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            startScale = startBounds.width().toFloat() / finalBounds.width()
            val startHeight = startScale * finalBounds.height()
            val deltaHeight = (startHeight - startBounds.height()) / 2
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        thumbView.alpha = 0f
        expandedImageView.visibility = View.VISIBLE
        expandedImageView.pivotX = 0f
        expandedImageView.pivotY = 0f

        val set = AnimatorSet()
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left.toFloat(), finalBounds.left.toFloat()))

                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top.toFloat(), finalBounds.top.toFloat()))

                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))

                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f))

        set.duration = mShortAnimationDuration!!.toLong()
        set.interpolator = DecelerateInterpolator()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mCurrentAnimator = null
            }

            override fun onAnimationCancel(animation: Animator) {
                mCurrentAnimator = null
            }
        })
        set.start()
        mCurrentAnimator = set

        expandedImageView.setOnClickListener {
            mCurrentAnimator?.cancel()

            val set = AnimatorSet()
            set.play(ObjectAnimator
                    .ofFloat(expandedImageView, View.X, startBounds.left.toFloat()))

                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,View.Y, startBounds.top.toFloat()))

                    .with(ObjectAnimator
                            .ofFloat(expandedImageView, View.SCALE_X, startScale))

                    .with(ObjectAnimator
                            .ofFloat(expandedImageView, View.SCALE_Y, startScale))

            set.duration = mShortAnimationDuration!!.toLong()
            set.interpolator = DecelerateInterpolator()
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    thumbView.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    mCurrentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    thumbView.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    mCurrentAnimator = null
                }
            })
            set.start()
            mCurrentAnimator = set
        }

    }
}
