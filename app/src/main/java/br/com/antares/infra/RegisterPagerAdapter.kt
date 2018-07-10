package br.com.antares.infra

import android.content.Context
import android.net.Uri
import android.support.v4.view.ViewPager
import android.view.ViewGroup
import br.com.antares.R
import br.com.antares.activities.ListRegisters
import br.com.antares.domain.register.Register
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter
import com.alexvasilkov.gestures.views.GestureImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * Define o Adapter que apresenta registros ao usuário, permitindo
 * maior controle sobre sua visualização.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class RegisterPagerAdapter(val context: Context) : RecyclePagerAdapter<RegisterPagerAdapter.ViewHolder>() {

    private var registers: List<Register> = mutableListOf()

    override fun onCreateViewHolder(container: ViewGroup): ViewHolder {
        context as ListRegisters
        val viewPager = context.findViewById<ViewPager>(R.id.recycler_pager)
        val holder = ViewHolder(container)
        holder.image.controller.enableScrollInViewPager(viewPager)
        return  holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val register = registers[position]
        loadFullImage(register, holder.image)
    }

    override fun getCount(): Int {
        return registers.size
    }

    fun updateLessons(registers: List<Register>) {
        this.registers = registers
        notifyDataSetChanged()
    }

    private fun loadFullImage(register: Register, imageView: GestureImageView) {

        val imageUri: Uri = Uri.parse(register.filepath)

        val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(Target.SIZE_ORIGINAL)
                .dontTransform()

        val thumbRequest = Glide.with(imageView)
                .load(imageUri)
                .thumbnail(0.1f)

        Glide.with(imageView)
                .load(imageUri)
                .apply(options)
                .thumbnail(thumbRequest)
                .into(imageView)

    }

    class ViewHolder(container: ViewGroup) : RecyclePagerAdapter.ViewHolder(GestureImageView(container.context)) {
        val image = itemView as GestureImageView
    }
}