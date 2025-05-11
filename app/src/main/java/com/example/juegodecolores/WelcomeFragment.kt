package com.example.juegodecolores

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.media.MediaPlayer
import android.view.animation.Animation
import android.widget.Button
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    private var mediaPlayer: MediaPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.musica_de_fondo_inicio)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        val botonJugar = view.findViewById<Button>(R.id.iniciarJuegoButton)
        botonJugar.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)
            botonJugar.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    requireActivity().supportFragmentManager.beginTransaction()
    .setCustomAnimations(
        R.anim.fade_in, // animación de entrada (GameFragment)
        R.anim.fade_out, // animación de salida (WelcomeFragment)
        R.anim.fade_in, // animación de entrada al volver (WelcomeFragment)
        R.anim.fade_out // animación de salida al volver (GameFragment)
    )
    .replace(R.id.FragmentContainer, GameFragment())
    .addToBackStack(null)
    .commit()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}