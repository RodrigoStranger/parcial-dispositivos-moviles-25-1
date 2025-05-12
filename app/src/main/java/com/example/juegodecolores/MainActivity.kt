package com.example.juegodecolores


/*
    Autor: Rodrigo Emerson Infanzon Acosta
    Curso: Programacion De Dispositivos Moviles
    Semestre: VI
    Fecha: 11/05/2025
    Ultima modificacion: 11/05/2025 20:05 pm
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

class MainActivity : AppCompatActivity() {
    private var musicaFondo: MediaPlayer? = null

    fun iniciarMusicaFondo(resId: Int) {
        musicaFondo?.stop()
        musicaFondo?.release()
        musicaFondo = MediaPlayer.create(this, resId)
        musicaFondo?.isLooping = true
        musicaFondo?.start()
    }

    fun pausarMusicaFondo() {
        musicaFondo?.pause()
    }

    fun reanudarMusicaFondo() {
        if (musicaFondo != null && !musicaFondo!!.isPlaying) {
            musicaFondo?.start()
        }
    }

    fun detenerMusicaFondo() {
        musicaFondo?.stop()
        musicaFondo?.release()
        musicaFondo = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()
        detenerMusicaFondo()
    }
}