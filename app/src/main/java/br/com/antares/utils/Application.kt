package br.com.antares.utils

import android.app.Application
import android.graphics.Bitmap
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig

/**
 * Classe responsável pela inicialização da biblioteca Fresco.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class Application : Application() {

    companion object {
        lateinit var instace: br.com.antares.utils.Application
    }

    override fun onCreate() {
        super.onCreate()
        instace = this
        val config = ImagePipelineConfig.newBuilder(this)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setDownsampleEnabled(true).build()
        Fresco.initialize(this, config)
    }
}