package br.com.antares.infra

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.antares.R
import br.com.antares.activities.ListRegisters
import br.com.antares.domain.register.Register
import com.alexvasilkov.gestures.views.GestureImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import java.io.File

/**
 * Define o Adapter que representa cada registros cadastrados
 * pelo usuário para que eles possam ser exibidos na RecyclerView.
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
        loadThumbImage(register, holder.imageView)
        holder.itemView.setOnClickListener {
            context as ListRegisters
            context.onRegisterClick(position)
        }
    }

    fun updateLessons(registers: List<Register>) {
        this.registers = registers
        notifyDataSetChanged()
    }

    class RegisterView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.photo_output)
    }

    private fun loadThumbImage(register: Register, imageView: AppCompatImageView) {

        val imageUri: Uri = Uri.parse(register.filepath)

        Glide.with(imageView)
                .load(imageUri)
                .thumbnail(0.1f)
                .into(imageView)
    }
}