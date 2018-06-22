package br.com.antares.extensions

import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView
import android.widget.Toast

/**
 * Exibe uma mensagem Toast com duração padrão de 2 segundos
 */
fun Activity.toast(message: CharSequence, leng: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, leng).show()

/**
 * Trata os eventos de clique
 */
fun Activity.onClick(@IdRes viewId: Int, onclick: (v: View?) -> Unit) {
    val view = findViewById<View>(viewId)
    view.setOnClickListener {onclick(it)}
}

/**
 * Recupera o conteúdo em texto de um TextView
 */
fun Activity.getTextString(@IdRes viewId: Int): String {
    val textview = findViewById<TextView>(viewId)
    return textview.text.toString()
}

/**
 * Retorna uma String a partir de um id de resource do tipo String(strings.xml)
 */
fun Activity.getResourceString(@StringRes resource: Int): String {
    return resources.getString(resource)
}