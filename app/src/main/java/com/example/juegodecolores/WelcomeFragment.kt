package com.example.juegodecolores

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.media.MediaPlayer
import android.view.animation.Animation
import android.widget.Button
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import android.content.pm.ActivityInfo

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.detenerMusicaFondo()
        (activity as? MainActivity)?.iniciarMusicaFondo(R.raw.musica_de_fondo_inicio)

        val botonJugar = view.findViewById<Button>(R.id.iniciarJuegoButton)
        botonJugar.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)
            // Detén la música de fondo inmediatamente
            (activity as? MainActivity)?.detenerMusicaFondo()
            botonJugar.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    findNavController().navigate(R.id.action_welcomeFragment_to_gameFragment)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        (activity as? MainActivity)?.reanudarMusicaFondo()
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        (activity as? MainActivity)?.pausarMusicaFondo()
    }
}