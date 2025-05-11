package com.example.juegodecolores

import android.os.CountDownTimer
import android.widget.TextView

class GameCountDownTimer(
    millisInFuture: Long,
    interval: Long = 1000L,
    private val countdownText: TextView,
    private val onFinishCallback: () -> Unit = {}
) : CountDownTimer(millisInFuture, interval) {
    override fun onTick(millisUntilFinished: Long) {
        val secondsLeft = millisUntilFinished / 1000
        countdownText.text = secondsLeft.toString()
        countdownText.visibility = android.view.View.VISIBLE
    }

    override fun onFinish() {
        countdownText.text = "¡Tiempo!"
        onFinishCallback()
    }
}
