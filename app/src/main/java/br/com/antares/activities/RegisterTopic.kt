package br.com.antares.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.antares.R
import br.com.antares.domain.course.Course
import br.com.antares.domain.topic.Topic
import br.com.antares.extensions.getTextString
import br.com.antares.infra.CourseService
import kotlinx.android.synthetic.main.activity_register_topic.*

/**
 *
 *
 * @author Augusto Santos
 * @version 1.0
 */
class RegisterTopic : AppCompatActivity() {

    private val courseService = CourseService(this)
    private var course: Course? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_topic)

        course = intent.extras.getParcelable(Course.COURSE)

        btn_new_topic_cancel.setOnClickListener { finish() }
        btn_new_topic_save.setOnClickListener { createTopic() }
    }

    private fun createTopic() {
        val topic = Topic(name = getTextString(R.id.et_topic_name),
                professorName = getTextString(R.id.et_topic_professor_name),
                description = getTextString(R.id.et_topic_description),
                course = course)

        courseService.createOrUpdateTopic(topic)
        finish()
    }
}
