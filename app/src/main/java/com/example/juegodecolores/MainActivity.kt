package com.example.juegodecolores


/*
    Autor: Rodrigo Emerson Infanzon Acosta
    Curso: Programacion De Dispositivos Moviles
    Semestre: VI
    Fecha: 11/05/2025
    Ultima modificacion: 11/05/2025 20:10 pm
*/

/*
    Crear un juego en el que el usuario deba presionar el botón que coincida con el 
    color que aparece en pantalla. El objetivo es hacer la mayor cantidad de aciertos 
    en 30 segundos.

    Qué debe tener la aplicación?
    1. Fragmento de bienvenida (WelcomeFragment):
    - Título del juego.
    - Breve explicación del juego.
    - Botón “Iniciar juego” para comenzar.
    - Interfaz libre pueden incluir imágenes.

    2. Fragmento del juego (GameFragment):
    - Cuadro que muestra un color (rojo, verde, azul, amarillo, etc).
    - Botones de colores para responder.
    - Cada acierto suma un punto y cambia el color.
    - Temporizador de 30 segundos.
    - Marcador que muestra los puntos obtenidos.

    3. Fragmento de resultados (ResultFragment):
    - Muestra el puntaje final.
    - Muestra un mensaje según el puntaje obtenido.
    - Botón para volver a jugar.
*/

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer

// Esta es la actividad principal de la aplicación, aquí gestiono el ciclo de vida y la música de fondo.
class MainActivity : AppCompatActivity() {
    // Declaro una variable para manejar la música de fondo durante el juego.
    private var musicaFondo: MediaPlayer? = null

    /**
     * Decidí poner las funciones relacionadas con la música de fondo en el
     * MainActivity porque esta actividad es el punto central de la aplicación y
     * está activa durante toda la experiencia del usuario. De esta manera,
     * puedo controlar la música de fondo de manera global, asegurando que
     * suene mientras el usuario navega entre fragmentos y se detenga o pause
     * correctamente según el ciclo de vida de la app.
     * Así evito tener que duplicar código en cada fragmento y centralizo el manejo de recursos
     * importantes como el MediaPlayer.
     */

    // Esta función la uso para iniciar la música de fondo cuando el usuario entra al juego.
    fun iniciarMusicaFondo(resId: Int) {
        // Si ya hay música sonando, primero la detengo para evitar que se mezclen sonidos.
        musicaFondo?.stop()
        // Libero los recursos del MediaPlayer anterior para no desperdiciar memoria.
        musicaFondo?.release()
        // Creo un nuevo MediaPlayer usando el recurso de audio que me pasan.
        musicaFondo = MediaPlayer.create(this, resId)
        // Configuro la música para que se repita constantemente mientras el usuario está en la app.
        musicaFondo?.isLooping = true
        // Inicio la música de fondo.
        musicaFondo?.start()
    }

    // Pauso la música de fondo, por ejemplo cuando el usuario sale temporalmente de la app.
    fun pausarMusicaFondo() {
        // Pauso la música si el usuario sale de la app o la minimiza.
        musicaFondo?.pause()
    }

    // Reanudo la música de fondo si estaba pausada y el usuario regresa a la app.
    fun reanudarMusicaFondo() {
        // Solo reanudo la música si existe el MediaPlayer y no está sonando.
        if (musicaFondo != null && !musicaFondo!!.isPlaying) {
            musicaFondo?.start() // Vuelvo a reproducir la música.
        }
    }

    // Detengo y libero los recursos de la música de fondo cuando ya no se necesita.
    fun detenerMusicaFondo() {
        // Detengo la música de fondo completamente.
        musicaFondo?.stop()
        // Libero los recursos del MediaPlayer para evitar fugas de memoria.
        musicaFondo?.release()
        // Pongo la variable en null para indicar que ya no hay música cargada.
        musicaFondo = null
    }

    // En el método onCreate, inicializo la actividad, habilito el modo edge-to-edge y establezco el layout principal.
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llamo al método padre para asegurar la inicialización estándar de la actividad.
        super.onCreate(savedInstanceState)
        // Habilito el modo edge-to-edge para que la app use toda la pantalla.
        enableEdgeToEdge()
        // Cargo el layout principal de la aplicación.
        setContentView(R.layout.activity_main)
    }

    // Cuando la actividad se destruye, me aseguro de detener la música y liberar recursos para evitar fugas de memoria.
    override fun onDestroy() {
        // Llamo al método padre para el cierre estándar de la actividad.
        super.onDestroy()
        // Me aseguro de detener la música y liberar recursos antes de cerrar la app.
        detenerMusicaFondo()
    }
}