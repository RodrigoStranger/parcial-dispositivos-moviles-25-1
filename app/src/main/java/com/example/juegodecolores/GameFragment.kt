package com.example.juegodecolores

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.media.MediaPlayer
import android.os.Looper

class GameFragment : Fragment(R.layout.fragment_game) {
    private var backgroundPlayer: MediaPlayer? = null
    private var warningPlayer: MediaPlayer? = null
    private var finishedPlayer: MediaPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hacer nada: bloquea la tecla atrás
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            startCountdown()
        }, 1000)
    }

    private fun startCountdown() {
        val countdownText = view?.findViewById<TextView>(R.id.countdown_text) ?: return
        val timerText = view?.findViewById<TextView>(R.id.timer_text) ?: return
        countdownText.visibility = View.VISIBLE
        timerText.visibility = View.VISIBLE // Siempre visible
        val countdownValues = listOf("3", "2", "1", "¡Empieza!")
        var index = 0

        // Sonido de conteo
        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.musica_conteo_3_2_1_empieza)
        mediaPlayer.setOnCompletionListener {
            it.release()
            // Inicia la música de fondo (solo una vez)
            if (backgroundPlayer == null) {
                backgroundPlayer = MediaPlayer.create(requireContext(), R.raw.musica_de_fondo_desarrollo_resumen)
                backgroundPlayer?.isLooping = true
                backgroundPlayer?.start()
            }
            // Oculta el texto de conteo y muestra el timer, inicia el temporizador
            countdownText.visibility = View.GONE
            timerText.visibility = View.VISIBLE
            // Inicia el temporizador
            var warningPlayed = false
            val gameTimer = GameCountDownTimer(
                30_000L,
                1000L,
                timerText,
                onTickCallback = { secondsLeft ->
                    if (secondsLeft <= 5 && !warningPlayed) {
                        warningPlayer?.release()
                        warningPlayer = MediaPlayer.create(requireContext(), R.raw.se_va_a_acabar_el_tiempo)
                        warningPlayer?.setOnCompletionListener { mp -> mp.release() }
                        warningPlayer?.start()
                        warningPlayed = true
                    }
                },
                onFinishCallback = {
                    finishedPlayer?.release()
                    finishedPlayer = MediaPlayer.create(requireContext(), R.raw.se_acabo_el_tiempo)
                    finishedPlayer?.setOnCompletionListener { mp -> mp.release() }
                    finishedPlayer?.start()
                }
            )
            gameTimer.start()
        }
        mediaPlayer.start()

        val handler = Handler(Looper.getMainLooper())
        lateinit var runnable: Runnable
        runnable = Runnable {
            if (index < countdownValues.size) {
                countdownText.text = countdownValues[index]
                index++
                handler.postDelayed(runnable, 1000)
            }
        }
        handler.post(runnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backgroundPlayer?.release()
        backgroundPlayer = null
        warningPlayer?.release()
        warningPlayer = null
        finishedPlayer?.release()
        finishedPlayer = null
    }

}