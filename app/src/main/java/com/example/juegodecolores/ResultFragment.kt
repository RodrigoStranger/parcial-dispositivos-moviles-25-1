package com.example.juegodecolores

import androidx.fragment.app.Fragment
import android.view.View
import android.os.Bundle

class ResultFragment : Fragment(R.layout.fragment_result) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Bloquear botón back
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hacer nada: bloquea la tecla atrás
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Bloquear rotación
        requireActivity().requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        // Permitir rotación al salir
        requireActivity().requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}