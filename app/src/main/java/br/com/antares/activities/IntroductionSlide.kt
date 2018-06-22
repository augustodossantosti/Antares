package br.com.antares.activities

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.Manifest
import android.content.Intent
import android.os.Bundle
import br.com.antares.R
import br.com.antares.utils.IntroVerifier
import br.com.antares.extensions.getResourceString
import br.com.antares.fragments.TermsConditionsSlide

/**
 * Activity responsável pela definição da tela de introdução do App.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class IntroductionSlide : MaterialIntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyIntroActivity()

        val neededPermissions = arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val possiblePermissions = arrayOf(Manifest.permission.RECORD_AUDIO)

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.slide1)
                .buttonsColor(R.color.slide_button)
                .title(getResourceString(R.string.slide_app_presentation_title))
                .description(getResourceString(R.string.slide_app_presentation_content))
                .image(R.drawable.books_intro2)
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.slide2)
                .buttonsColor(R.color.slide_button)
                .title(getResourceString(R.string.slide_camera_permission_title))
                .description(getResourceString(R.string.slide_camera_permission_content))
                .image(R.drawable.camera_intro)
                .neededPermissions(neededPermissions)
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.slide3)
                .buttonsColor(R.color.slide_button)
                .title(getResourceString(R.string.slide_audio_record_permission_title))
                .description(getResourceString(R.string.slide_audio_record_permission_content))
                .image(R.drawable.mic_intro)
                .possiblePermissions(possiblePermissions)
                .build())

        addSlide(TermsConditionsSlide())
    }

    /**
     * Verifica se a activity deve ser exibida ao usuário.
     */
    private fun verifyIntroActivity() {
        if (IntroVerifier(this).isIntroActivityShown()) {
            startActivity(Intent(this, ListCourses::class.java))
            finish()
        }
    }

}
