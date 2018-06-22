package br.com.antares.infra

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.antares.R
import br.com.antares.activities.ListTopics
import br.com.antares.domain.course.Course

/**
 * Define o Adapter que representa cada registro de cursos cadastrados
 * pelo usuário para que eles possam ser exibidos na RecyclerView.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class CourseAdapter(private val context: Context) : RecyclerView.Adapter<CourseAdapter.CourseView>() {

    private var courses: List<Course> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseView {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_course, parent, false)
        return CourseView(view)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    override fun onBindViewHolder(holder: CourseView, position: Int) {
        val course = courses[position]
        holder.name.text = "${course.name}"
        holder.institution.text = "${course.institution}"
        holder.description.text = "${course.description}"

        holder.parentView.setOnClickListener {
            val intent = Intent(context, ListTopics::class.java)
            intent.putExtra(Course.COURSE, course)
            context.startActivity(intent)
        }
    }

    fun updateCourses(courses: List<Course>) {
        this.courses = courses
        notifyDataSetChanged()
    }

    /**
     *  Define o ViewHolder que contém os dados referentes a cursos
     *  cadastrados pelo usuário.
     */
    class CourseView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.course_name)
        val institution: TextView = itemView.findViewById(R.id.course_institution)
        val description: TextView = itemView.findViewById(R.id.course_description)
        val parentView: View = itemView
    }
}