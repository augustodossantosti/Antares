package br.com.antares.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import br.com.antares.R
import br.com.antares.domain.course.Course
import br.com.antares.domain.topic.Topic
import br.com.antares.extensions.onClick
import br.com.antares.infra.CourseService
import br.com.antares.infra.TopicAdapter
import kotlinx.android.synthetic.main.activity_list_topics.*

/**
 * Activity responsável pela apresentação dos topicos
 * salvos pelo usuário.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class ListTopics : BaseActivity() {

    private val courseService = CourseService(this)
    private val topicAdapter = TopicAdapter(this)
    private var course: Course? = null
    private val topics = ArrayList<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_topics)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_topics_title)

        recycler_topic.setHasFixedSize(false)
        recycler_topic.layoutManager = LinearLayoutManager(this)
        recycler_topic.adapter = topicAdapter

        course = intent.extras.getParcelable(Course.COURSE)
        topics.addAll(savedInstanceState?.getParcelableArrayList(Topic.TOPIC_LIST) ?: courseService.listAllTopics(course?.id))
        updateList(topics)

        onClick(R.id.new_topic) {
            val intent = Intent(this, RegisterTopic::class.java)
            intent.putExtra(Course.COURSE, course)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        topics.clear()
        topics.addAll(courseService.listAllTopics(course?.id))
        updateList(topics)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelableArrayList(Topic.TOPIC_LIST, topics)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        courseService.closeConnection()
        super.onDestroy()
    }

    /**
     * Atualiza a listagem de tópicos exibida ao usuário.
     */
    private fun updateList(topics: List<Topic>) {
        topicAdapter.updateTopics(topics)
    }

}
