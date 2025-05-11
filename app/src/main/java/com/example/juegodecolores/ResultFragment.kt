package com.example.juegodecolores

import androidx.fragment.app.Fragment
import android.view.View
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class ResultFragment : Fragment(R.layout.fragment_result) {
    private var puntuacionPlayer: android.media.MediaPlayer? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Recupera el score enviado por argumentos con Bundle
        val score = arguments?.getInt("score") ?: 0
        // Muestra el puntaje en el TextView
        val textViewResult = view.findViewById<TextView>(R.id.textViewResult)
        textViewResult.text = getString(R.string.resultado_text, score)
        // Reproduce el sonido de puntuación
        puntuacionPlayer = android.media.MediaPlayer.create(requireContext(), R.raw.sonido_puntuacion)
        puntuacionPlayer?.setOnCompletionListener {
            it.release()
            puntuacionPlayer = null
        }
        puntuacionPlayer?.start()
        // Animación bounce para los botones
        val volverMenu = view.findViewById<android.widget.Button>(R.id.volverMenuInicioButton)
        val volverJugar = view.findViewById<android.widget.Button>(R.id.volverAJugarButton)
        volverMenu.setOnClickListener {
            val bounce = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)
            bounce.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {}
                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    puntuacionPlayer?.stop()
                    puntuacionPlayer?.release()
                    puntuacionPlayer = null
                    (activity as? MainActivity)?.detenerMusicaFondo()
                    (activity as? MainActivity)?.iniciarMusicaFondo(R.raw.musica_de_fondo_inicio)
                    findNavController().navigate(R.id.action_resultFragment_to_welcomeFragment)
                }
            })
            it.startAnimation(bounce)
        }
        volverJugar.setOnClickListener {
            val bounce = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)
            bounce.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {}
                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    findNavController().popBackStack()
                }
            })
            it.startAnimation(bounce)
        }
        // Bloquear botón back
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hacer nada: bloquea la tecla atrás
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        puntuacionPlayer?.stop()
        puntuacionPlayer?.release()
        puntuacionPlayer = null
    }

    override fun onResume() {
        super.onResume()
        // Bloquear rotación
        requireActivity().requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        // Permitir rotación al salir
        requireActivity().requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}