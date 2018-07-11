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
    private val holders = mutableListOf<ViewHolder>()
    private val containerActivity = context as ListCourses

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_course, parent, false)
        val viewHolder = ViewHolder(view)
        holders.add(viewHolder)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]
        holder.name.text = "${course.name}"
        holder.institution.text = "${course.institution}"

        holder.itemView.setOnClickListener { onItemClick(holder, course) }
        holder.itemView.setOnLongClickListener { onItemSelected(holder, course) }
    }

    fun updateCourses(courses: List<Course>) {
        this.courses = courses
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<Course> {
        return courses.filter { it.isSelected }
    }

    fun removeSelections() {
        val selectedHolders = holders.filter { it.isSelected }
        selectedHolders.forEach { it.removeSelect() }
    }

    private fun onItemClick(holder: ViewHolder, course: Course) {

        if (course.isSelected) {
            holder.removeSelect()
            course.isSelected = false
        } else {
            val intent = Intent(context, ListTopics::class.java)
            intent.putExtra(Course.COURSE, course)
            context.startActivity(intent)
        }
    }

    private fun onItemSelected(holder: ViewHolder, course: Course): Boolean {
        holder.setSelected()
        course.isSelected = true
        if (!containerActivity.isActionModeActive()) {
            containerActivity.showContextMenu()
        }
        return true
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.course_name)
        val institution: TextView = itemView.findViewById(R.id.course_institution)
        var isSelected = false

        fun setSelected() {
            val selectedImageView: AppCompatImageView? = itemView.findViewById(R.id.is_selected_image)
            selectedImageView?.visibility = View.VISIBLE
            itemView.setBackgroundColor(Color.LTGRAY)
            isSelected = true
        }

        fun removeSelect() {
            val selectedImageView: AppCompatImageView? = itemView.findViewById(R.id.is_selected_image)
            selectedImageView?.visibility = View.INVISIBLE
            itemView.setBackgroundColor(Color.TRANSPARENT)
            isSelected = false
        }
    }
}