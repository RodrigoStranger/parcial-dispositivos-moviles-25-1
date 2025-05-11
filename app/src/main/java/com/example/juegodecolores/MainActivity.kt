package com.example.juegodecolores

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