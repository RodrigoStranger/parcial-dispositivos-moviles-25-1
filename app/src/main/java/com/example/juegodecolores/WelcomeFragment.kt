package com.example.juegodecolores

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.widget.Button
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import android.content.pm.ActivityInfo

// Este fragmento es la pantalla de bienvenida del juego. Aquí muestro el título,
// una breve explicación y el botón para iniciar la partida. Centralizo la lógica de
// inicio y el control de la música de fondo para que la experiencia sea
// fluida desde el primer contacto del usuario.

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    /**
     * Se llama cuando la vista del fragmento ha sido creada.
     * Aquí inicializo la música de fondo y configuro el botón para empezar el juego.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Siempre me aseguro de detener cualquier música previa antes de iniciar la música de bienvenida.
        (activity as? MainActivity)?.detenerMusicaFondo() // Detengo la música de fondo.
        // Inicio la música de fondo específica para la pantalla de bienvenida.
        (activity as? MainActivity)?.iniciarMusicaFondo(R.raw.musica_de_fondo_inicio) // Inicio la música de fondo.

        // Busco el botón para iniciar el juego.
        val botonJugar = view.findViewById<Button>(R.id.iniciarJuegoButton)
        // Cuando el usuario toca el botón, hago una animación y navego al juego.
        botonJugar.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.boton_bounce)
            // Detengo la música de fondo antes de pasar al juego para evitar que se solape con la música del juego.
            (activity as? MainActivity)?.detenerMusicaFondo() // Detengo la música de fondo.
            botonJugar.startAnimation(anim) // Aplico la animación al botón.
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {} // No se usa.
                override fun onAnimationRepeat(animation: Animation?) {} // No se usa.
                override fun onAnimationEnd(animation: Animation?) { // Cuando termina la animación
                    // Cuando termina la animación, navego al fragmento del juego.
                    findNavController().navigate(R.id.action_welcomeFragment_to_gameFragment)
                }
            })
        }
    }

    /**
     * Cuando el usuario vuelve a la pantalla de bienvenida, bloqueo la
     * rotación y reanudo la música de fondo.
     */

    override fun onResume() {
        super.onResume() // Llamada al metodo de la clase padre
        // Bloqueo la pantalla en modo vertical para mantener la interfaz consistente.
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Reanudo la música de fondo si estaba pausada.
        (activity as? MainActivity)?.reanudarMusicaFondo()
    }

    /**
     * Cuando el usuario sale de la pantalla de bienvenida,
     * permito la rotación y pauso la música de fondo.
     */

    override fun onPause() {
        super.onPause() // Llamada al metodo de la clase padre
        // Permito que la pantalla rote libremente cuando salgo de este fragmento.
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        // Pauso la música de fondo para ahorrar recursos.
        (activity as? MainActivity)?.pausarMusicaFondo()
    }
}