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

// Este fragmento es el núcleo del juego. Aquí gestiono la lógica principal:
// muestro el color objetivo, manejo los botones de selección, controlo el temporizador,
// la puntuación y los sonidos. Toda la experiencia interactiva del usuario ocurre en este fragmento.
class GameFragment : Fragment(R.layout.fragment_game) {
    private var hasNavigated = false // Evita doble navegación
    private var countdownPlayer: MediaPlayer? = null // Uso un MediaPlayer para reproducir el sonido de conteo
    private var countdownPosition: Int = 0 // Uso esta variable para pausar el sonido de conteo
    private var countdownIndex: Int = 0 // Uso esta variable para pausar el sonido de conteo
    private var isCountdownPaused: Boolean = false // Uso esta variable para pausar el sonido de conteo
    private var countdownHandler: Handler? = null // Handler para el conteo
    private var countdownRunnable: Runnable? = null // Runnable para el conteo
    // Elimina  y usa la música centralizada en MainActivity
    private var warningPlayer: MediaPlayer? = null // Sonido de advertencia
    private var finishedPlayer: MediaPlayer? = null // Sonido de fin
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
    private var currentColors: List<ColorOption> = emptyList() // Colores disponibles
    private var targetColor: ColorOption? = null // Color objetivo
    private var score = 0 // Puntuación actual
    private var gameActive = false // Flag para saber si el juego esta activo

    /**
     * Inicia una nueva ronda del juego: selecciono los colores, asigno los botones y
     * muestro el color objetivo.
     */

    // Devuelvo la lista de botones principales del juego
    private fun getGameButtons(): List<Button> =
        listOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4).mapNotNull { view?.findViewById<Button>(it) }

    // Cambio la visibilidad de todos los botones del juego
    private fun setButtonsVisibility(visibility: Int) {
        getGameButtons().forEach { it.visibility = visibility }
    }

    // Habilito o deshabilito todos los botones del juego
    private fun setButtonsEnabled(enabled: Boolean) {
        getGameButtons().forEach { it.isEnabled = enabled }
    }

    // Libero y limpio un MediaPlayer
    private fun releaseMediaPlayer(player: MediaPlayer?): MediaPlayer? {
        player?.setOnCompletionListener(null)
        player?.release()
        return null
    }

    private fun iniciarNuevaRonda() {
        if (!gameActive) return
        // Selecciono 4 colores aleatorios para esta ronda.
        currentColors = colorOptions.shuffled().take(4)
        // Elijo uno de esos colores como el objetivo a adivinar.
        targetColor = currentColors.random()
        // Asigno cada color a un botón y guardo el nombre como tag para comparar después.
        val buttons = getGameButtons()
        for (i in 0..3) {
            val btn = buttons.getOrNull(i) // Si no hay mas botones, no hago nada
            btn?.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                    requireContext().getColor(
                        currentColors[i].colorRes // Uso el color correspondiente
                    )
                )
            )
            btn?.tag = currentColors[i].name // Así sé qué color representa cada botón.
        }
        setButtonsEnabled(true) // Habilito todos los botones para la nueva ronda.
        // Muestro el nombre del color objetivo que el usuario debe identificar.
        val colorNameText = view?.findViewById<TextView>(R.id.color_dynamic_text)
        colorNameText?.text = targetColor?.name
    }

    /**
     * Muestra el resultado de la selección del usuario (acierto/error),
     * reproduce el sonido correspondiente y prepara la siguiente ronda.
     */

    private fun mostrarResultado(acierto: Boolean) {
        // Muestro una imagen y reproduzco un sonido según si el usuario acertó o no.
        val imageView = view?.findViewById<ImageView>(R.id.game_image)
        val mediaPlayer = if (acierto) {
            imageView?.setImageResource(R.drawable.bien) // Imagen de acierto
            MediaPlayer.create(requireContext(), R.raw.sonido_correcto) // Sonido de acierto
        } else {
            imageView?.setImageResource(R.drawable.error) // Imagen de error
            MediaPlayer.create(requireContext(), R.raw.sonido_error) // Sonido de error
        }
        imageView?.visibility = View.VISIBLE // Si no es nulo, muestro la imagen.
        mediaPlayer.setOnCompletionListener {
            imageView?.visibility = View.INVISIBLE
            mediaPlayer.release()
            // Si el juego sigue activo, inicio la siguiente ronda automáticamente.
            if (gameActive) iniciarNuevaRonda()
        }
        mediaPlayer.start()
    }

    /**
     * Se llama cuando la vista del fragmento ha sido creada. Aquí inicializo los botones,
     * oculto elementos, y preparo el juego para el usuario.
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparo la animación bounce para dar feedback visual en los botones.
        val bounce = AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)

        // Al iniciar el fragmento, oculto todos los elementos del juego para mostrarlos solo cuando corresponda.
        setButtonsVisibility(View.INVISIBLE)
        // Oculto el texto "Adivina el color" y el texto dinámico.
        val colorNameText = view.findViewById<TextView>(R.id.color_name_text)
        colorNameText.visibility = View.INVISIBLE
        colorNameText.setTextColor(requireContext().getColor(R.color.blanco))
        val colorDynamicText = view.findViewById<TextView>(R.id.color_dynamic_text)
        colorDynamicText.visibility = View.INVISIBLE
        // Oculto la puntuación al inicio.
        val scoreText = view.findViewById<TextView>(R.id.score_text)
        scoreText.visibility = View.INVISIBLE

        val buttons = getGameButtons()

        // Para cada botón, asigno el listener que gestiona la lógica de selección de color.
        buttons.forEach { btn ->
            btn.setOnClickListener { v ->
                v.startAnimation(bounce) // Feedback visual al pulsar
                if (!gameActive) return@setOnClickListener // Ignoro si el juego no está activo
                // Desactivo todos los botones para evitar múltiples respuestas en una ronda
                setButtonsEnabled(false)
                val elegido = btn.tag as? String // Qué color eligió el usuario
                val correcto = targetColor?.name // Cuál era el color correcto
                if (elegido != null && correcto != null) {
                    if (elegido == correcto) {
                        // Si acierta, sumo un punto y actualizo la puntuación
                        score++
                        scoreText.text = getString(R.string.score_text, score)
                        mostrarResultado(true)
                    } else {
                        // Si falla, muestro el feedback de error
                        mostrarResultado(false)
                    }
                }
            }
        }

        // Bloqueo el botón físico "atrás" para que el usuario no salga accidentalmente del juego.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Bloqueo la tecla de atrás.
            }
        })

        // Solo inicia el conteo la primera vez
        // Si el conteo no está pausado (primera vez), lo inicio.
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
            setButtonsVisibility(View.VISIBLE)
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
            timerText.setTextColor(requireContext().getColor(R.color.blanco))
            var warningPlayed = false // Marca si se ha reproducido el sonido de advertencia
            gameTimer = object : android.os.CountDownTimer(tiempoRestante, 50L) {  
                override fun onTick(millisUntilFinished: Long) { // Cada 50ms
                    tiempoRestante = millisUntilFinished
                    val minutes = (millisUntilFinished / 1000) / 60 // Minutos restantes
                    val seconds = (millisUntilFinished / 1000) % 60 // Segundos restantes
                    // Formato mm:ss
                    val timeFormatted = String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds)
                    timerText.text = timeFormatted
                    val secondsLeft = (millisUntilFinished / 1000).toInt() + if (millisUntilFinished % 1000 > 0) 1 else 0
                    if (secondsLeft <= 5) { // Si queda menos de 5 segundos
                        timerText.setTextColor(requireContext().getColor(R.color.rojo)) // Cambio el color del texto
                    } else {
                        timerText.setTextColor(requireContext().getColor(R.color.blanco)) // Cambio el color del texto
                    }
                    if (secondsLeft <= 5 && !warningPlayed) { // Si queda menos de 5 segundos y no se ha reproducido el sonido de advertencia
                        warningPlayer?.release() // Libero el sonido de advertencia
                        warningPlayer = MediaPlayer.create(requireContext(), R.raw.se_va_a_acabar_el_tiempo)
                        warningPlayer?.setOnCompletionListener { mp -> mp.release() } // Libero el sonido de advertencia
                        warningPlayer?.start() // Reproduzco el sonido de advertencia
                        warningPlayed = true // Marca que se ha reproducido el sonido de advertencia
                    }
                }
                override fun onFinish() {
                    tiempoRestante = 0L
                    timerText.text = getString(R.string.timer_zero)
                    finishedPlayer?.release()
                    finishedPlayer = MediaPlayer.create(requireContext(), R.raw.se_acabo_el_tiempo)
                    finishedPlayer?.setOnCompletionListener { mp ->
                        mp.release() // Libero el sonido de fin
                        if (!hasNavigated && isAdded && view != null) { // Si no se ha navegado y la vista sigue siendo visible
                            hasNavigated = true
                            try {
                                // Envio el puntaje al ResultFragment
                                val bundle = Bundle()
                                bundle.putInt("score", score)
                                // Navego al ResultFragment
                                findNavController().navigate(
                                    R.id.action_gameFragment_to_resultFragment, // Accion de navegacion
                                    bundle // Puntaje
                                )
                            } catch (e: Exception) {
                                e.printStackTrace() // Imprimo el error
                            }
                        }
                    }
                    finishedPlayer?.start() // Reproduzco el sonido de fin
                    gameActive = false // Marca que el juego no esta activo
                    setButtonsEnabled(false) // Deshabilito los botones
                }
            }
            gameTimer?.start() // Inicio el temporizador
        }
        if (!isCountdownPaused) {
            countdownIndex = 0 // Inicializo el conteo
        }
        countdownPlayer?.start() // Reproduzco el sonido de conteo
        // Inicio el conteo
        countdownRunnable?.let { countdownHandler?.removeCallbacks(it) }
        countdownHandler = Handler(Looper.getMainLooper())
        countdownRunnable = object : Runnable { // Runnable para el conteo
            override fun run() {
                val pos = countdownPlayer?.currentPosition ?: 0
                val idx = countdownTimes.indexOfLast { pos >= it }
                if (idx in countdownValues.indices) { // Si el indice esta dentro del rango
                    countdownText.text = countdownValues[idx]
                }
                if (countdownPlayer?.isPlaying == true) { // Si el sonido de conteo esta reproduciendo
                    countdownHandler?.postDelayed(this, 50) // Reproduzco el sonido de conteo
                }
            }
        }
        countdownRunnable?.let { countdownHandler?.postDelayed(it, 0) } // Inicio el conteo
    }

    // Libero los recursos al destruir la vista
    override fun onDestroyView() {
        super.onDestroyView()

        warningPlayer = releaseMediaPlayer(warningPlayer) // Libero el sonido de advertencia
        finishedPlayer = releaseMediaPlayer(finishedPlayer) // Libero el sonido de fin
        hasNavigated = false // Reinicia flag al destruir vista
    }

    // Se llama cuando el fragmento se vuelve visible
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
            val timerText = view?.findViewById<TextView>(R.id.timer_text) ?: return
            timerText.setTextColor(requireContext().getColor(R.color.blanco))
            var warningPlayed = false
            gameTimer = object : android.os.CountDownTimer(tiempoRestante, 50L) {
                override fun onTick(millisUntilFinished: Long) {
                    tiempoRestante = millisUntilFinished
                    val minutes = (millisUntilFinished / 1000) / 60
                    val seconds = (millisUntilFinished / 1000) % 60
                    // Formato mm:ss
                    val timeFormatted = String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds)
                    timerText.text = timeFormatted
                    val secondsLeft = (millisUntilFinished / 1000).toInt() + if (millisUntilFinished % 1000 > 0) 1 else 0
                    if (secondsLeft <= 5) { // Si queda menos de 5 segundos
                        timerText.setTextColor(requireContext().getColor(R.color.rojo)) // Cambio el color del texto
                    } else { // Si queda mas de 5 segundos
                        timerText.setTextColor(requireContext().getColor(R.color.blanco)) // Cambio el color del texto
                    } 
                    if (secondsLeft <= 5 && !warningPlayed) { // Si queda menos de 5 segundos y no se ha reproducido el sonido de advertencia
                        warningPlayer?.release() // Libero el sonido de advertencia
                        warningPlayer = MediaPlayer.create(requireContext(), R.raw.se_va_a_acabar_el_tiempo)
                        warningPlayer?.setOnCompletionListener { mp -> mp.release() }  // Libero el sonido de advertencia
                        warningPlayer?.start() // Reproduzco el sonido de advertencia
                        warningPlayed = true // Marca que se ha reproducido el sonido de advertencia
                    }
                }
                override fun onFinish() {
                    tiempoRestante = 0L
                    timerText.text = getString(R.string.timer_zero)
                    finishedPlayer?.release()
                    finishedPlayer = MediaPlayer.create(requireContext(), R.raw.se_acabo_el_tiempo)
                    finishedPlayer?.setOnCompletionListener { mp ->
                        mp.release() // Libero el sonido de fin
                        if (!hasNavigated && isAdded && view != null) { // Si no se ha navegado y el fragmento esta agregado
                            hasNavigated = true // Marca que se ha navegado
                            try {
                                val bundle = Bundle() // Creo el bundle
                                bundle.putInt("score", score) // Agrego el puntaje
                                findNavController().navigate( // Navego al ResultFragment
                                    R.id.action_gameFragment_to_resultFragment, // Accion de navegacion
                                    bundle // Puntaje
                                )
                            } catch (e: Exception) {
                                e.printStackTrace() // Imprimo el error
                            }
                        }
                    } // Fin del temporizador
                    finishedPlayer?.start() // Reproduzco el sonido de fin
                    gameActive = false // Marca que el juego no esta activo
                    setButtonsEnabled(false) // Deshabilito los botones
                }
            }
            gameTimer?.start() // Inicio el temporizador
        }
    }

    override fun onPause() { // Se llama cuando el fragmento se vuelve invisible
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