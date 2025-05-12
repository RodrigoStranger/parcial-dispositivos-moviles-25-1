package com.example.juegodecolores

import androidx.fragment.app.Fragment
import android.view.View
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.fragment.findNavController

// Este fragmento se encarga de mostrar el resultado final del juego al usuario, reproducir un sonido especial de puntuación y gestionar la navegación de vuelta al menú o para volver a jugar.
// Centralizo aquí la lógica de resultado para mantener el código modular y fácil de mantener.
class ResultFragment : Fragment(R.layout.fragment_result) {
    // Uso este MediaPlayer para reproducir el sonido de puntuación cuando el usuario ve su resultado.
    private var puntuacionPlayer: android.media.MediaPlayer? = null

    /**
     * Se llama cuando el fragmento se crea y se inicializa la vista.
     * Aquí se configuran los elementos de la interfaz y se establecen los listeners para los eventos.
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupero el puntaje que se pasó como argumento desde el GameFragment.
        val score = arguments?.getInt("score") ?: 0

        // Busco el TextView donde voy a mostrar el resultado final.
        val textViewResult = view.findViewById<TextView>(R.id.textViewResult)

        // Muestro el puntaje usando el string de recursos para mantener el formato.
        textViewResult.text = getString(R.string.resultado_text, score)

        // Creo y reproduzco el sonido de puntuación para dar feedback auditivo al usuario.
        puntuacionPlayer = android.media.MediaPlayer.create(requireContext(), R.raw.sonido_puntuacion)

        // Cuando termina el sonido, libero los recursos del MediaPlayer.
        puntuacionPlayer?.setOnCompletionListener {
            it.release()
            puntuacionPlayer = null
        }
        puntuacionPlayer?.start()

        // Preparo los botones para volver al menú o volver a jugar.
        val volverMenu = view.findViewById<android.widget.Button>(R.id.volverMenuInicioButton)
        val volverJugar = view.findViewById<android.widget.Button>(R.id.volverAJugarButton)

        // Cuando el usuario toca "Volver al menú", hago una animación bounce y cambio la música de fondo.
        volverMenu.setOnClickListener {
            val bounce = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)
            bounce.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {}
                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    // Detengo y libero el sonido de puntuación para evitar que siga sonando.
                    puntuacionPlayer?.stop()
                    puntuacionPlayer?.release()
                    puntuacionPlayer = null

                    // Cambio la música de fondo a la del menú principal.
                    (activity as? MainActivity)?.detenerMusicaFondo()
                    (activity as? MainActivity)?.iniciarMusicaFondo(R.raw.musica_de_fondo_inicio)

                    // Navego hacia el fragmento de bienvenida.
                    findNavController().navigate(R.id.action_resultFragment_to_welcomeFragment)
                }
            })
            it.startAnimation(bounce)
        }

        // Cuando el usuario toca "Volver a jugar", hago la animación y navego hacia atrás para reiniciar el juego.
        volverJugar.setOnClickListener {
            val bounce = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)
            bounce.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {}
                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    // Detengo la música y regreso al fragmento anterior para reiniciar el juego.
                    (activity as? MainActivity)?.detenerMusicaFondo()
                    findNavController().popBackStack()
                }
            })
            it.startAnimation(bounce)
        }

        // Bloqueo la función del botón físico "atrás" para que el usuario no salga accidentalmente de la pantalla de resultados.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hago nada aquí para bloquear la tecla atrás.
            }
        })
    }

    /**
     * Se llama cuando el fragmento se destruye.
     * Aquí libero los recursos que no se necesitan más.
     */

    override fun onDestroyView() {
        super.onDestroyView()
        puntuacionPlayer?.stop()
        puntuacionPlayer?.release()
        puntuacionPlayer = null
    }

    // Cuando el usuario vuelve a este fragmento, bloqueo la rotación para que la interfaz se mantenga estable y reinicio la música de fondo específica de la pantalla de resultados.
    override fun onResume() {
        super.onResume()
        // Bloqueo la rotación de pantalla en modo vertical para evitar problemas de UI.
        requireActivity().requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Si el fragmento está visible, inicio la música de fondo de resultados.
        if (isVisible) {
            (activity as? MainActivity)?.iniciarMusicaFondo(R.raw.musica_de_fondo_desarrollo_resumen)
        }
    }

    override fun onPause() {
        super.onPause()
        // Permitir rotación al salir
        requireActivity().requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        (activity as? MainActivity)?.detenerMusicaFondo()
    }
}