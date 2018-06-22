package br.com.antares.utils

import android.content.Context
import android.support.v4.app.FragmentActivity

/**
 * Controla a exibição da tela de introdução do App.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class IntroVerifier(private val context: FragmentActivity) {

    /**
     * Atualiza a frag que determina se os slides de
     * introdução já foram exibidos.
     */
    fun updateIntroStatus(status: Boolean) {
        context
                .getSharedPreferences("PREF", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("1", status)
                .apply()
    }

    /**
     * Verifica se a Activity dos slides de introdução
     * já foi exibida para o usuário anteriormente.
     */
    fun isIntroActivityShown() = context
            .getSharedPreferences("PREF", Context.MODE_PRIVATE)
            .getBoolean("1", false)
}