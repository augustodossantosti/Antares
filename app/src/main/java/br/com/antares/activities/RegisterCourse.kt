package br.com.antares.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.antares.R
import br.com.antares.domain.course.Course
import br.com.antares.extensions.getTextString
import br.com.antares.infra.CourseService
import kotlinx.android.synthetic.main.activity_register_course.*

/**
 * Tela para cadastro de novos cursos.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class RegisterCourse : AppCompatActivity() {

    private val courseService = CourseService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_course)
        btn_new_course_save.setOnClickListener { createCourse() }
        btn_new_course_cancel.setOnClickListener { finish() }
    }

    private fun createCourse() {
        val course = Course(name = getTextString(R.id.et_course_name),
                institution = getTextString(R.id.et_instituition_name))

        courseService.createOrUpdateCourse(course)
        finish()
    }

    override fun onDestroy() {
        courseService.closeConnection()
        super.onDestroy()
    }

}
