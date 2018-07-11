package br.com.antares.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import br.com.antares.R
import br.com.antares.domain.course.Course
import br.com.antares.extensions.onClick
import br.com.antares.extensions.toast
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
class ListCourses : BaseActivity(), ActionMode.Callback {

    private val courseService = CourseService(this)
    private val courseAdapter = CourseAdapter(this)
    private val courses = ArrayList<Course>()
    private var actionMode : ActionMode? = null

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        toolbar?.inflateMenu(R.menu.menu_toolbar)
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.item_selection_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        courseAdapter.removeSelections()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        val itemId = item?.itemId

        if (itemId == R.id.rename) {
            toast("Rename")
            mode?.finish()
            return true
        }
        if (itemId == R.id.delete) {
            deleteSelectedItemns()
            mode?.finish()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        if (actionMode != null) {
            actionMode?.finish()
            actionMode = null
            courseAdapter.removeSelections()
        } else {
            super.onBackPressed()
        }
    }

    fun showContextMenu() {
        actionMode = startSupportActionMode(this)
    }

    fun isActionModeActive(): Boolean {
        return actionMode != null
    }

    private fun deleteSelectedItemns() {
        val itemsToDelete = courseAdapter.getSelectedItems()
        courseService.deleteCourses(itemsToDelete)
        courses.removeAll(itemsToDelete)
        updateList(courses)
    }

    private fun updateList(courses: List<Course>) {
        courseAdapter.updateCourses(courses)
    }
}
