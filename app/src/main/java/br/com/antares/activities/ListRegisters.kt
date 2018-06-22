package br.com.antares.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import br.com.antares.R
import br.com.antares.domain.lessons.Lesson
import br.com.antares.domain.register.Register
import br.com.antares.domain.register.Type
import br.com.antares.extensions.onClick
import br.com.antares.extensions.toast
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {

            val register = Register(type = Type.PHOTO, filepath = currentPhotoPath.toString(), lesson = lesson)
            courseService.createOrUpdateRegister(register)
            registers.add(register)
            updateList(registers)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
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
}
