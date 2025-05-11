package com.example.juegodecolores

import androidx.fragment.app.Fragment
import android.view.View
import android.os.Bundle
import android.widget.TextView

class ResultFragment : Fragment(R.layout.fragment_result) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Recupera el score enviado por argumentos con Bundle
        val score = arguments?.getInt("score") ?: 0
        // Muestra el puntaje en el TextView
        val textViewResult = view.findViewById<TextView>(R.id.textViewResult)
        textViewResult.text = getString(R.string.resultado_text, score)
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