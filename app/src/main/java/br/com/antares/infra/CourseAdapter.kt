package br.com.antares.infra

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.antares.R
import br.com.antares.activities.ListCourses
import br.com.antares.activities.ListTopics
import br.com.antares.domain.course.Course

/**
 * Define o Adapter que representa cada registro de cursos cadastrados
 * pelo usuário para que eles possam ser exibidos na RecyclerView.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class CourseAdapter(private val context: Context) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    private var courses: List<Course> = mutableListOf()
    private val containerActivity = context as ListCourses

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_course, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]
        holder.name.text = "${course.name}"
        holder.institution.text = "${course.institution}"

        holder.itemView.setOnClickListener { onItemClick(it, course) }
        holder.itemView.setOnLongClickListener { onItemSelected(it, course) }
    }

    fun updateCourses(courses: List<Course>) {
        this.courses = courses
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<Course> {
        return courses.filter { it.isSelected }
    }

    private fun onItemClick(view: View, course: Course) {

        if (course.isSelected) {
            val selectedImageView: AppCompatImageView? = view.findViewById(R.id.is_selected_image)
            selectedImageView?.visibility = View.INVISIBLE
            view.setBackgroundColor(Color.TRANSPARENT)
            course.isSelected = false
        } else {
            val intent = Intent(context, ListTopics::class.java)
            intent.putExtra(Course.COURSE, course)
            context.startActivity(intent)
        }
    }

    private fun onItemSelected(view: View, course: Course): Boolean {
        val selectedImageView: AppCompatImageView? = view.findViewById(R.id.is_selected_image)
        selectedImageView?.visibility = View.VISIBLE
        view.setBackgroundColor(Color.LTGRAY)
        course.isSelected = true
        containerActivity.showContextMenu()
        return true
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.course_name)
        val institution: TextView = itemView.findViewById(R.id.course_institution)
    }
}