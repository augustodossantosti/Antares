package br.com.antares.infra

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.antares.R
import br.com.antares.activities.ListRegisters
import br.com.antares.domain.register.Register
import com.alexvasilkov.gestures.views.GestureImageView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import java.io.File

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

        val cursor = context.contentResolver.query(Uri.parse(register.filepath),
                Array(1) { MediaStore.Images.ImageColumns.DATA},
                null, null, null)
        cursor.moveToFirst()
        val photoPath = cursor.getString(0)
        cursor.close()
        val file = File(photoPath)
        val uri = Uri.fromFile(file)
        val height = context.resources.getDimensionPixelSize(R.dimen.photo_height)
        val width = context.resources.getDimensionPixelSize(R.dimen.photo_width)

        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(ResizeOptions(width, height))
                .build()

        val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(holder.photoView.controller)
                .setImageRequest(request)
                .build()

        holder.photoView.controller = controller

        holder.parentView.setOnClickListener {
            context as ListRegisters
            context.zoomImageFromThumb(holder.parentView, uri)
//            val gestureImageView = context.findViewById<GestureImageView>(R.id.photo_output_full)
//            gestureImageView.setImageURI(uri)
//            gestureImageView.visibility = View.VISIBLE
        }
    }

    fun updateLessons(registers: List<Register>) {
        this.registers = registers
        notifyDataSetChanged()
    }

    class RegisterView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoView: SimpleDraweeView = itemView.findViewById(R.id.photo_output)
        val parentView = itemView
    }
}