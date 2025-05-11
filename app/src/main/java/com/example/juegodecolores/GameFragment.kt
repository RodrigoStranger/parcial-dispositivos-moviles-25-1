package com.example.juegodecolores

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import android.os.Handler
import android.os.Looper
import android.widget.TextView

class GameFragment : Fragment(R.layout.fragment_game) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Espera a que termine el fade-in (1000ms) antes de iniciar el contador
        view.postDelayed({
            startCountdown()
        }, 1000)

    }

    private var mediaPlayer: MediaPlayer? = null
    private var backgroundPlayer: MediaPlayer? = null

    private fun startCountdown() {
        val countdownText = view?.findViewById<TextView>(R.id.countdown_text) ?: return
        countdownText.visibility = View.VISIBLE
        val countdownValues = listOf("3", "2", "1", "¡Empieza!")
        var index = 0

        // Inicia el audio del conteo
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.musica_conteo_3_2_1_empieza)
        mediaPlayer?.setOnCompletionListener {
            // Cuando termine el audio del conteo, inicia la música de fondo
            backgroundPlayer = MediaPlayer.create(requireContext(), R.raw.musica_de_fondo_desarrollo_resumen)
            backgroundPlayer?.isLooping = true
            backgroundPlayer?.start()
        }
        mediaPlayer?.start()

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (index < countdownValues.size) {
                    countdownText.text = countdownValues[index]
                    index++
                    handler.postDelayed(this, 1000)
                } else {
                    countdownText.visibility = View.GONE
                    // Aquí puedes iniciar el juego/temporizador real
                }
            }
        }
        handler.post(runnable)
    }
}