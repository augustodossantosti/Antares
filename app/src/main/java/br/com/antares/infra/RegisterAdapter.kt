package br.com.antares.infra

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import br.com.antares.R
import br.com.antares.domain.register.Register
import java.io.IOException
import java.io.InputStream

/**
 *
 *
 * @author Augusto Santos
 * @version 1.0
 */
class RegisterAdapter(private val context: Context) : RecyclerView.Adapter<RegisterAdapter.RegisterView>() {

    private var registers: List<Register> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterView {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_register, parent, false)
        return RegisterAdapter.RegisterView(view)
    }

    override fun getItemCount(): Int {
        return registers.size
    }

    override fun onBindViewHolder(holder: RegisterView, position: Int) {
        val register = registers[position]
        try {
            val imageStream: InputStream = context.contentResolver.openInputStream(Uri.parse(register.filepath))
            holder.photoView.setImageBitmap(BitmapFactory.decodeStream(imageStream))
        } catch (exception: IOException) {
            println(exception.stackTrace)
        }

        holder.parentView.setOnClickListener {
            Toast.makeText(context, "Clicou!", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateLessons(registers: List<Register>) {
        this.registers = registers
        notifyDataSetChanged()
    }

    class RegisterView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoView: AppCompatImageView = itemView.findViewById(R.id.photo_view)
        val parentView = itemView
    }
}