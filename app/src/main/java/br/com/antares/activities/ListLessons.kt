package br.com.antares.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import br.com.antares.R
import br.com.antares.domain.lessons.Lesson
import br.com.antares.domain.register.Register
import br.com.antares.domain.register.Type
import br.com.antares.domain.topic.Topic
import br.com.antares.extensions.onClick
import br.com.antares.infra.CourseService
import br.com.antares.infra.LessonAdapter
import kotlinx.android.synthetic.main.activity_list_lessons.*
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

/**
 * Activity responsável pela apresentação das lissões
 * salvas pelo usuário.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class ListLessons : BaseActivity() {

    private val courseService = CourseService(this)
    private val lessonAdapter = LessonAdapter(this)
    private val lessons = ArrayList<Lesson>()
    private var topic: Topic? = null
    private var currentPhotoPath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_lessons)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_lessons_title)

        recycler_lesson.setHasFixedSize(false)
        recycler_lesson.layoutManager = LinearLayoutManager(this)
        recycler_lesson.adapter = lessonAdapter

        topic = intent.extras.getParcelable(Topic.TOPIC)
        lessons.addAll(savedInstanceState?.getParcelableArrayList(Lesson.LESSON_LIST)
                ?: courseService.listAllLessons(topic?.id))
        updateList(lessons)

        onClick(R.id.new_lesson) {
            val intent = Intent(this, RegisterLesson::class.java)
            intent.putExtra(Topic.TOPIC, topic)
            startActivityForResult(intent, 0)
        }

        onClick(R.id.new_register) {
            currentPhotoPath = takeAPhoto()
        }
    }

    override fun onResume() {
        super.onResume()
        lessons.clear()
        lessons.addAll(courseService.listAllLessons(topic?.id))
        updateList(lessons)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {

            val lesson = createLessonWithRegister()
            val intent = Intent(this, ListRegisters::class.java)
            intent.putExtra(Lesson.LESSON, lesson)
            startActivity(intent)

        } else {
            lessons.clear()
            lessons.addAll(courseService.listAllLessons(topic?.id))
            updateList(lessons)
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelableArrayList(Lesson.LESSON_LIST, lessons)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        courseService.closeConnection()
        super.onDestroy()
    }

    private fun updateList(lessons: List<Lesson>) {
        lessonAdapter.updateLessons(lessons)
    }

    private fun createLessonWithRegister(): Lesson {
        val lesson = Lesson(subject = "", description = "", create_Date = Date(), topic = topic)
        courseService.createOrUpdateLesson(lesson)
        val register = Register(type = Type.PHOTO, order = 1, filepath = currentPhotoPath.toString(), lesson = lesson)
        courseService.createOrUpdateRegister(register)
        lesson.registers = Arrays.asList(register)
        return lesson
    }
}
