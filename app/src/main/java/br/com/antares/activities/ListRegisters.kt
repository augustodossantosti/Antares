package br.com.antares.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.antares.R
import br.com.antares.domain.lessons.Lesson
import br.com.antares.domain.register.Register
import br.com.antares.domain.register.Type
import br.com.antares.extensions.onClick
import br.com.antares.infra.CourseService
import br.com.antares.infra.RegisterPagerAdapter
import br.com.antares.infra.RegisterAdapter
import com.alexvasilkov.gestures.transition.GestureTransitions
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker
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
    private val pageAdapter = RegisterPagerAdapter(this)
    private var lesson: Lesson? = null
    private val registers = ArrayList<Register>()
    private var currentPhotoPath: Uri? = null
    private var animator: ViewsTransitionAnimator<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_registers)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_registers_title)

        recycler_register.setHasFixedSize(false)
        recycler_register.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recycler_register.adapter = registerAdapter

        recycler_pager.adapter = pageAdapter
        recycler_pager.pageMargin = resources.getDimensionPixelSize(R.dimen.view_pager_margin)

        lesson = intent.extras.getParcelable(Lesson.LESSON)
        registers.addAll(savedInstanceState?.getParcelableArrayList(Register.REGISTER_LIST) ?: (lesson?.registers?.toList() ?: emptyList()))
        updateList(registers)

        animator = GestureTransitions.from(recycler_register, object : SimpleTracker() {
            override fun getViewAt(position: Int): View? {
                val holder = recycler_register.findViewHolderForLayoutPosition(position)
                return if (holder != null) (holder as RegisterAdapter.RegisterView).imageView else null
            }
        }).into(recycler_pager, object : SimpleTracker() {
            override fun getViewAt(position: Int): View? {
                val holder = pageAdapter.getViewHolder(position)
                return holder?.image
            }
        })

        animator?.addPositionUpdateListener { pos, _ -> applyImageAnimationState(pos) }

        onClick(R.id.new_register) {
            currentPhotoPath = takeAPhoto()
        }

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

    override fun onBackPressed() {
        if (!animator!!.isLeaving) {
            animator!!.exit(true)
        } else {
            super.onBackPressed()
        }
    }

    fun onRegisterClick(position: Int) {
        animator!!.enter(position, true)
    }

    private fun updateList(lessons: List<Register>) {
        registerAdapter.updateLessons(lessons)
        pageAdapter.updateLessons(lessons)
    }

    private fun applyImageAnimationState(position: Float) {
        recycler_full_background.visibility = if (position == 0f) View.INVISIBLE else View.VISIBLE
        recycler_full_background.alpha = position
    }
}
