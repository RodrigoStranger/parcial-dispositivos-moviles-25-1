package com.example.juegodecolores

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.media.MediaPlayer

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    private var mediaPlayer: MediaPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.musica_de_fondo_inicio)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}