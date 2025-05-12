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
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

class GameFragment : Fragment(R.layout.fragment_game) {
    private var hasNavigated = false // Evita doble navegación
    private var countdownPlayer: MediaPlayer? = null
    private var countdownPosition: Int = 0
    private var countdownIndex: Int = 0
    private var isCountdownPaused: Boolean = false
    private var countdownHandler: Handler? = null
    private var countdownRunnable: Runnable? = null
    // Elimina  y usa la música centralizada en MainActivity
    private var warningPlayer: MediaPlayer? = null
    private var finishedPlayer: MediaPlayer? = null
    // --- TEMPORIZADOR ---
    private var tiempoRestante: Long = 30_000L
    private var gameTimer: android.os.CountDownTimer? = null

    // --- Lógica de colores ---
    data class ColorOption(val name: String, val colorRes: Int)

    private val colorOptions = listOf(
        ColorOption("Rojo", R.color.rojo),
        ColorOption("Violeta", R.color.violeta),
        ColorOption("Naranja", R.color.naranja),
        ColorOption("Verde", R.color.verde),
        ColorOption("Azul", R.color.azul),
        ColorOption("Rosado", R.color.rosado),
        ColorOption("Amarillo", R.color.amarillo),
        ColorOption("Marrón", R.color.marron),
        ColorOption("Negro", R.color.negro),
        ColorOption("Blanco", R.color.blanco)
    )
    private var currentColors: List<ColorOption> = emptyList()
    private var targetColor: ColorOption? = null
    private var score = 0
    private var gameActive = false

    private fun iniciarNuevaRonda() {
        if (!gameActive) return
        // Selecciona 4 colores distintos
        currentColors = colorOptions.shuffled().take(4)
        // Selecciona uno de esos como objetivo
        targetColor = currentColors.random()
        // Asigna colores a los botones
        val buttonIds = listOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4)
        for (i in 0..3) {
            val btn = view?.findViewById<Button>(buttonIds[i])
            btn?.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                    requireContext().getColor(
                        currentColors[i].colorRes
                    )
                )
            )
            btn?.tag = currentColors[i].name // Guarda el nombre del color en el tag
            btn?.isEnabled = true // Habilita el botón para la nueva ronda
        }
        // Muestra el nombre del color objetivo
        val colorNameText = view?.findViewById<TextView>(R.id.color_dynamic_text)
        colorNameText?.text = targetColor?.name
    }

    private fun mostrarResultado(acierto: Boolean) {
        val imageView = view?.findViewById<ImageView>(R.id.game_image)
        val mediaPlayer = if (acierto) {
            imageView?.setImageResource(R.drawable.bien) // bien.png
            MediaPlayer.create(requireContext(), R.raw.sonido_correcto)
        } else {
            imageView?.setImageResource(R.drawable.error) // mal.png
            MediaPlayer.create(requireContext(), R.raw.sonido_error)
        }
        imageView?.visibility = View.VISIBLE
        mediaPlayer.setOnCompletionListener {
            imageView?.visibility = View.INVISIBLE
            mediaPlayer.release()
            // Siguiente ronda si el juego sigue activo
            if (gameActive) iniciarNuevaRonda()
        }
        mediaPlayer.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animación bounce para los 4 botones
        val bounce = AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)
        val button1 = view.findViewById<Button>(R.id.button1)
        val button2 = view.findViewById<Button>(R.id.button2)
        val button3 = view.findViewById<Button>(R.id.button3)
        val button4 = view.findViewById<Button>(R.id.button4)

        // Oculta los botones al inicio
        button1.visibility = View.INVISIBLE
        button2.visibility = View.INVISIBLE
        button3.visibility = View.INVISIBLE
        button4.visibility = View.INVISIBLE
        // Oculta el texto 'Adivina el color' al inicio
        val colorNameText = view.findViewById<TextView>(R.id.color_name_text)
        colorNameText.visibility = View.INVISIBLE
        colorNameText.setTextColor(requireContext().getColor(R.color.blanco))
        // Oculta el texto 'nombre color' dinámico al inicio
        val colorDynamicText = view.findViewById<TextView>(R.id.color_dynamic_text)
        colorDynamicText.visibility = View.INVISIBLE
        // Oculta la puntuación al inicio
        val scoreText = view.findViewById<TextView>(R.id.score_text)
        scoreText.visibility = View.INVISIBLE

        val buttonIds = listOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4)
        val buttons = buttonIds.map { view.findViewById<Button>(it) }

        buttons.forEach { btn ->
            btn.setOnClickListener { v ->
                v.startAnimation(bounce)
                if (!gameActive) return@setOnClickListener
                // Permite solo un click por ronda
                buttons.forEach { it.isEnabled = false }
                val elegido = btn.tag as? String
                val correcto = targetColor?.name
                if (elegido != null && correcto != null) {
                    if (elegido == correcto) {
                        score++
                        scoreText.text = getString(R.string.score_text, score)
                        mostrarResultado(true)
                    } else {
                        mostrarResultado(false)
                    }
                }
            }
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
            (activity as? MainActivity)?.iniciarMusicaFondo(R.raw.musica_de_fondo_desarrollo_resumen)

            // Oculta el texto de conteo y muestra el timer, inicia el temporizador
            countdownText.visibility = View.GONE
            timerText.visibility = View.VISIBLE
            // Muestra los botones ahora
            val button1 = view?.findViewById<Button>(R.id.button1)
            val button2 = view?.findViewById<Button>(R.id.button2)
            val button3 = view?.findViewById<Button>(R.id.button3)
            val button4 = view?.findViewById<Button>(R.id.button4)
            button1?.visibility = View.VISIBLE
            button2?.visibility = View.VISIBLE
            button3?.visibility = View.VISIBLE
            button4?.visibility = View.VISIBLE
            // Muestra el texto 'Adivina el color' ahora
            val colorNameText = view?.findViewById<TextView>(R.id.color_name_text)
            colorNameText?.visibility = View.VISIBLE
            // Muestra el texto 'nombre color' dinámico ahora
            val colorDynamicText = view?.findViewById<TextView>(R.id.color_dynamic_text)
            colorDynamicText?.visibility = View.VISIBLE
            // Muestra la puntuación ahora
            val scoreText = view?.findViewById<TextView>(R.id.score_text)
            scoreText?.visibility = View.VISIBLE
            // Activa el juego y resetea la puntuación
            gameActive = true
            score = 0
            scoreText?.text = getString(R.string.score_text, 0)
            iniciarNuevaRonda()
            // Inicia el temporizador
            tiempoRestante = 30_000L
            val timerText = view?.findViewById<TextView>(R.id.timer_text)
            timerText?.setTextColor(requireContext().getColor(R.color.blanco))
            var warningPlayed = false
            gameTimer = object : android.os.CountDownTimer(tiempoRestante, 50L) {
                override fun onTick(millisUntilFinished: Long) {
                    tiempoRestante = millisUntilFinished
                    val minutes = (millisUntilFinished / 1000) / 60
val seconds = (millisUntilFinished / 1000) % 60
// Formato mm:ss
val timeFormatted = String.format("%02d:%02d", minutes, seconds)
timerText?.text = timeFormatted
                    val secondsLeft = (millisUntilFinished / 1000).toInt() + if (millisUntilFinished % 1000 > 0) 1 else 0
                    if (secondsLeft <= 5) {
                        timerText?.setTextColor(requireContext().getColor(R.color.rojo))
                    } else {
                        timerText?.setTextColor(requireContext().getColor(R.color.blanco))
                    }
                    if (secondsLeft <= 5 && !warningPlayed) {
                        warningPlayer?.release()
                        warningPlayer = MediaPlayer.create(requireContext(), R.raw.se_va_a_acabar_el_tiempo)
                        warningPlayer?.setOnCompletionListener { mp -> mp.release() }
                        warningPlayer?.start()
                        warningPlayed = true
                    }
                }
                override fun onFinish() {
                    tiempoRestante = 0L
                    timerText?.text = "00:00"
                    finishedPlayer?.release()
                    finishedPlayer = MediaPlayer.create(requireContext(), R.raw.se_acabo_el_tiempo)
                    finishedPlayer?.setOnCompletionListener { mp ->
                        mp.release()
                        if (!hasNavigated && isAdded && view != null) {
                            hasNavigated = true
                            try {
                                val bundle = Bundle()
                                bundle.putInt("score", score)
                                findNavController().navigate(
                                    R.id.action_gameFragment_to_resultFragment,
                                    bundle
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    finishedPlayer?.start()
                    gameActive = false
                    val buttonIds = listOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4)
                    buttonIds.forEach { id ->
                        view?.findViewById<Button>(id)?.isEnabled = false
                    }
                }
            }
            gameTimer?.start()
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

        warningPlayer?.release()
        warningPlayer = null
        finishedPlayer?.setOnCompletionListener(null)
        finishedPlayer?.release()
        finishedPlayer = null
        hasNavigated = false // Reinicia flag al destruir vista
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        (activity as? MainActivity)?.reanudarMusicaFondo()
        // Si el conteo estaba pausado, reanuda audio y texto
        if (isCountdownPaused) {
            countdownPlayer?.seekTo(countdownPosition)
            countdownPlayer?.start()
            countdownRunnable?.let { countdownHandler?.postDelayed(it, 0) }
            isCountdownPaused = false
        }
        // --- TEMPORIZADOR: solo reanuda con el tiempo restante ---
        if (gameActive && tiempoRestante > 0) {
            val timerText = view?.findViewById<TextView>(R.id.timer_text)
            timerText?.setTextColor(requireContext().getColor(R.color.blanco))
            var warningPlayed = false
            gameTimer = object : android.os.CountDownTimer(tiempoRestante, 50L) {
                override fun onTick(millisUntilFinished: Long) {
                    tiempoRestante = millisUntilFinished
                    val minutes = (millisUntilFinished / 1000) / 60
val seconds = (millisUntilFinished / 1000) % 60
// Formato mm:ss
val timeFormatted = String.format("%02d:%02d", minutes, seconds)
timerText?.text = timeFormatted
                    val secondsLeft = (millisUntilFinished / 1000).toInt() + if (millisUntilFinished % 1000 > 0) 1 else 0
                    if (secondsLeft <= 5) {
                        timerText?.setTextColor(requireContext().getColor(R.color.rojo))
                    } else {
                        timerText?.setTextColor(requireContext().getColor(R.color.blanco))
                    }
                    if (secondsLeft <= 5 && !warningPlayed) {
                        warningPlayer?.release()
                        warningPlayer = MediaPlayer.create(requireContext(), R.raw.se_va_a_acabar_el_tiempo)
                        warningPlayer?.setOnCompletionListener { mp -> mp.release() }
                        warningPlayer?.start()
                        warningPlayed = true
                    }
                }
                override fun onFinish() {
                    tiempoRestante = 0L
                    timerText?.text = "00:00"
                    finishedPlayer?.release()
                    finishedPlayer = MediaPlayer.create(requireContext(), R.raw.se_acabo_el_tiempo)
                    finishedPlayer?.setOnCompletionListener { mp ->
                        mp.release()
                        if (!hasNavigated && isAdded && view != null) {
                            hasNavigated = true
                            try {
                                val bundle = Bundle()
                                bundle.putInt("score", score)
                                findNavController().navigate(
                                    R.id.action_gameFragment_to_resultFragment,
                                    bundle
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    finishedPlayer?.start()
                    gameActive = false
                    val buttonIds = listOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4)
                    buttonIds.forEach { id ->
                        view?.findViewById<Button>(id)?.isEnabled = false
                    }
                }
            }
            gameTimer?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.pausarMusicaFondo()
        // Pausar conteo 3-2-1 si está activo
        if (countdownPlayer?.isPlaying == true) {
            countdownPlayer?.pause()
            countdownPosition = countdownPlayer?.currentPosition ?: 0
            isCountdownPaused = true
        }
        // Pausar el handler del texto
        countdownRunnable?.let { countdownHandler?.removeCallbacks(it) }
        // --- TEMPORIZADOR: cancela y guarda tiempo restante ---
        gameTimer?.cancel()
        // tiempoRestante ya se actualiza en cada onTick
    }
}