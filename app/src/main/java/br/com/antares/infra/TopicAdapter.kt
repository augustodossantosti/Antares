package br.com.antares.infra

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import br.com.antares.R
import br.com.antares.activities.ListLessons
import br.com.antares.domain.topic.Topic

/**
 * Define o Adapter que representa cada registro de topicos cadastrados
 * pelo usuário para que eles possam ser exibidos na RecyclerView.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class TopicAdapter(private val context: Context) : RecyclerView.Adapter<TopicAdapter.TopicView>() {

    private var topics: List<Topic> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicView {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_topic, parent, false)
        return TopicView(view)
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    override fun onBindViewHolder(holder: TopicView, position: Int) {
        val topic = topics[position]
        holder.name.text = "${topic.name}"
        holder.professorName.text = "${topic.professorName}"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ListLessons::class.java)
            intent.putExtra(Topic.TOPIC, topic)
            context.startActivity(intent)
        }
    }

    fun updateTopics(topics: List<Topic>) {
        this.topics = topics
        notifyDataSetChanged()
    }

    class TopicView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.topic_name)
        val professorName: TextView = itemView.findViewById(R.id.topic_professor)
    }
}