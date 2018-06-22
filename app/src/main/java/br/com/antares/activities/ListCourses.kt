package br.com.antares.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import br.com.antares.R
import br.com.antares.domain.course.Course
import br.com.antares.extensions.onClick
import br.com.antares.infra.CourseAdapter
import br.com.antares.infra.CourseService
import kotlinx.android.synthetic.main.activity_list_courses.*

/**
 * Activity responsável pela apresentação dos cursos
 * salvos pelo usuário.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class ListCourses : BaseActivity() {

    private val courseService = CourseService(this)
    private val courseAdapter = CourseAdapter(this)
    private val courses = ArrayList<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_courses)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_courses_title)

        recycler_course.setHasFixedSize(false)
        recycler_course.layoutManager = LinearLayoutManager(this)
        recycler_course.adapter = courseAdapter

        onClick(R.id.new_course) {
            startActivityForResult(Intent(this, RegisterCourse::class.java), 0)
        }

        courses.addAll(savedInstanceState?.getParcelableArrayList(Course.COURSE_LIST) ?: courseService.listAllCourses())
        updateList(courses)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        courses.clear()
        courses.addAll(courseService.listAllCourses())
        updateList(courses)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelableArrayList(Course.COURSE_LIST, courses)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        courseService.closeConnection()
        super.onDestroy()
    }

    /**
     * Atualiza a listagem de cursos exibida ao usuário.
     */
    private fun updateList(courses: List<Course>) {
        courseAdapter.updateCourses(courses)
    }
}
