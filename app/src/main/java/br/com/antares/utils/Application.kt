package br.com.antares.utils

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

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
        Fresco.initialize(this)
    }
}