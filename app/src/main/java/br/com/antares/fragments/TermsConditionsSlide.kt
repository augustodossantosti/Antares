package br.com.antares.fragments

import agency.tango.materialintroscreen.SlideFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.antares.R
import br.com.antares.activities.ListCourses
import br.com.antares.utils.IntroVerifier
import br.com.antares.extensions.getResourceString
import kotlinx.android.synthetic.main.fragment_terms_conditions_slide.*

/**
 * Define o fragment que forma a tela de termos e condições de
 * uso do App apresentada no slide de introdução.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class TermsConditionsSlide : SlideFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_terms_conditions_slide,
                container, false)
    }

    /**
     * Verifica se o usuário aceitou os termos de uso para
     * que ele possa prosseguir na execução do app.
     */
    override fun canMoveFurther(): Boolean {
        if (cb_agree.isChecked) {
            IntroVerifier(activity!!).updateIntroStatus(true)

            val intent = Intent(activity, ListCourses::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            activity?.finish()
        }
        return cb_agree.isChecked
    }

    /**
     * Define a mensagem a ser retornada ao usuário caso ele
     * deixe de marcar o checkbox de termos de uso.
     */
    override fun cantMoveFurtherErrorMessage(): String? {
        return activity?.getResourceString(R.string.slide_use_terms_checkbox_error)
    }

    override fun backgroundColor(): Int {
        return R.color.slide4
    }

    override fun buttonsColor(): Int {
        return R.color.slide_button
    }
}