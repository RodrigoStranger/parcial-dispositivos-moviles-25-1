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
                    // Inicia el temporizador del juego
                    val timerText = view?.findViewById<TextView>(R.id.timer_text) ?: return
                    timerText.visibility = View.VISIBLE
                    var warningPlayed = false
val gameTimer = GameCountDownTimer(
    30_000L,
    1000L,
    timerText,
    onTickCallback = { secondsLeft ->
        if (secondsLeft <= 5 && !warningPlayed) {
            val warningPlayer = MediaPlayer.create(requireContext(), R.raw.se_va_a_acabar_el_tiempo)
            warningPlayer.setOnCompletionListener { mp -> mp.release() }
            warningPlayer.start()
            warningPlayed = true
        }
    },
    onFinishCallback = {
        val finishedPlayer = MediaPlayer.create(requireContext(), R.raw.se_acabo_el_tiempo)
        finishedPlayer.setOnCompletionListener { mp -> mp.release() }
        finishedPlayer.start()
    }
)
gameTimer.start()
                    gameTimer.start()
                }
            }
        }
        handler.post(runnable)
    }
}