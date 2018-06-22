package br.com.antares.infra

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.antares.R
import br.com.antares.activities.ListRegisters
import br.com.antares.domain.lessons.Lesson

/**
 * Define o Adapter que representa cada registro de lissões cadastrados
 * pelo usuário para que eles possam ser exibidos na RecyclerView.
 *
 * @author Augusto Santos
 * @version 1.o
 */
class LessonAdapter(private val context: Context) : RecyclerView.Adapter<LessonAdapter.LessonView>() {

    private var lessons: List<Lesson> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonView {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_lesson, parent, false)
        return LessonView(view)
    }

    override fun getItemCount(): Int {
        return lessons.size
    }

    override fun onBindViewHolder(holder: LessonView, position: Int) {
        val lesson = lessons[position]
        holder.subject.text = "${lesson.subject}"
        holder.createDate.text = "${lesson.create_Date}"
        holder.description.text = "${lesson.description}"
        holder.registersQuatity.text = "${lesson.registersQuantity()}"
        holder.parentView.setOnClickListener {
            val intent = Intent(context, ListRegisters::class.java)
            intent.putExtra(Lesson.LESSON, lesson)
            context.startActivity(intent)
        }
    }

    fun updateLessons(lessons: List<Lesson>) {
        this.lessons = lessons
        notifyDataSetChanged()
    }

    /**
     *  Define o ViewHolder que contém os dados referentes a lissões
     *  cadastrados pelo usuário.
     */
    class LessonView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject: TextView = itemView.findViewById(R.id.lesson_subject)
        val createDate: TextView = itemView.findViewById(R.id.lesson_create_date)
        val description: TextView = itemView.findViewById(R.id.lesson_description)
        val registersQuatity: TextView = itemView.findViewById(R.id.lesson_register_quantity)
        val parentView = itemView
    }
}