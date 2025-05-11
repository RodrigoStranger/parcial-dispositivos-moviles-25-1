package com.example.juegodecolores

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Button
import androidx.fragment.app.Fragment
import android.media.MediaPlayer
import android.os.Looper
import android.view.animation.AnimationUtils

class GameFragment : Fragment(R.layout.fragment_game) {
    private var countdownPlayer: MediaPlayer? = null
    private var countdownPosition: Int = 0
    private var countdownIndex: Int = 0
    private var isCountdownPaused: Boolean = false
    private var countdownHandler: Handler? = null
    private var countdownRunnable: Runnable? = null
    private var backgroundPlayer: MediaPlayer? = null
    private var warningPlayer: MediaPlayer? = null
    private var finishedPlayer: MediaPlayer? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animación bounce para los 4 botones
        val bounce = AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)
        val button1 = view.findViewById<Button>(R.id.button1)
        val button2 = view.findViewById<Button>(R.id.button2)
        val button3 = view.findViewById<Button>(R.id.button3)
        val button4 = view.findViewById<Button>(R.id.button4)

        button1.setOnClickListener { v ->
            v.startAnimation(bounce)
            // Lógica adicional para el botón 1
        }
        button2.setOnClickListener { v ->
            v.startAnimation(bounce)
            // Lógica adicional para el botón 2
        }
        button3.setOnClickListener { v ->
            v.startAnimation(bounce)
            // Lógica adicional para el botón 3
        }
        button4.setOnClickListener { v ->
            v.startAnimation(bounce)
            // Lógica adicional para el botón 4
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hacer nada: bloquea la tecla atrás
            }
        })

        // Solo inicia el conteo la primera vez
        if (!isCountdownPaused) {
            startCountdown()
        }
    }

    private fun startCountdown() {
        val countdownText = view?.findViewById<TextView>(R.id.countdown_text) ?: return
        val timerText = view?.findViewById<TextView>(R.id.timer_text) ?: return
        countdownText.visibility = View.VISIBLE
        timerText.visibility = View.VISIBLE // Siempre visible
        val countdownValues = listOf("3", "2", "1", "¡Empieza!")
        // Ajusta estos tiempos según tu audio (en milisegundos)
        val countdownTimes = listOf(0, 1000, 2000, 3000)

        // Sonido de conteo
        countdownPlayer?.release()
        countdownPlayer = MediaPlayer.create(requireContext(), R.raw.musica_conteo_3_2_1_empieza)
        countdownPlayer?.seekTo(countdownPosition)
        countdownPlayer?.setOnCompletionListener {
            it.release()
            countdownPlayer = null
            countdownPosition = 0
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
                    if (secondsLeft <= 5) {
                        timerText.setTextColor(requireContext().getColor(R.color.rojo))
                    } else {
                        timerText.setTextColor(requireContext().getColor(R.color.blanco))
                    }
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
        if (!isCountdownPaused) {
            countdownIndex = 0
        }
        countdownPlayer?.start()

        countdownRunnable?.let { countdownHandler?.removeCallbacks(it) }
        countdownHandler = Handler(Looper.getMainLooper())
        countdownRunnable = object : Runnable {
            override fun run() {
                val pos = countdownPlayer?.currentPosition ?: 0
                val idx = countdownTimes.indexOfLast { pos >= it }
                if (idx in countdownValues.indices) {
                    countdownText.text = countdownValues[idx]
                }
                if (countdownPlayer?.isPlaying == true) {
                    countdownHandler?.postDelayed(this, 50)
                }
            }
        }
        countdownRunnable?.let { countdownHandler?.postDelayed(it, 0) }
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

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        backgroundPlayer?.start()
        // Si el conteo estaba pausado, solo reanuda audio y handler
        if (isCountdownPaused) {
            countdownPlayer?.seekTo(countdownPosition)
            countdownPlayer?.start()
            countdownRunnable?.let { countdownHandler?.postDelayed(it, 0) }
            isCountdownPaused = false
        }
    }

    override fun onPause() {
        super.onPause()
        backgroundPlayer?.pause()
        warningPlayer?.pause()
        finishedPlayer?.pause()
        countdownPlayer?.pause()
        countdownPosition = countdownPlayer?.currentPosition ?: 0
        isCountdownPaused = true
        countdownRunnable?.let { countdownHandler?.postDelayed(it, 0) }
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

}