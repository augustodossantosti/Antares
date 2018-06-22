package br.com.antares.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.antares.R
import br.com.antares.domain.lessons.Lesson
import br.com.antares.domain.topic.Topic
import br.com.antares.extensions.getTextString
import br.com.antares.infra.CourseService
import kotlinx.android.synthetic.main.activity_register_lesson.*
import java.util.*

/**
 *
 *
 * @author Augusto Santos
 * @version 1.0
 */
class RegisterLesson : AppCompatActivity() {

    private val courseService = CourseService(this)
    private var topic: Topic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_lesson)

        topic = intent.extras.getParcelable(Topic.TOPIC)

        btn_new_lesson_cancel.setOnClickListener { finish() }
        btn_new_lesson_save.setOnClickListener { createLesson() }
    }

    private fun createLesson() {
        val lesson = Lesson(subject = getTextString(R.id.et_lesson_subject),
                description = getTextString(R.id.et_lesson_description),
                create_Date = Date(),
                topic = topic)

        courseService.createOrUpdateLesson(lesson)
        finish()
    }
}
