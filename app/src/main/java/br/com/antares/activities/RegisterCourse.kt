package br.com.antares.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.antares.R
import br.com.antares.domain.course.Course
import br.com.antares.extensions.getTextString
import br.com.antares.extensions.onClick
import br.com.antares.extensions.toast
import br.com.antares.infra.CourseService

/**
 *
 *
 * @author Augusto Santos
 * @version 1.0
 */
class RegisterCourse : AppCompatActivity() {

    private val courseService = CourseService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_course)
        onClick(R.id.btn_register) {createCourse()}
    }

    private fun createCourse() {
        val course = Course(name = getTextString(R.id.et_course_name),
                institution = getTextString(R.id.et_instituition_name),
                description = getTextString(R.id.et_description))

        if (courseService.createOrUpdateCourse(course)) {
            finish()
        } else toast(getString(R.string.error_message))
    }

    override fun onDestroy() {
        courseService.closeConnection()
        super.onDestroy()
    }

}
