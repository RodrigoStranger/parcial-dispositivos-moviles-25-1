package com.example.juegodecolores

import android.os.CountDownTimer
import android.widget.TextView

class GameCountDownTimer(
    millisInFuture: Long,
    interval: Long = 1000L,
    private val countdownText: TextView,
    private val onTickCallback: ((secondsLeft: Long) -> Unit)? = null,
    private val onFinishCallback: () -> Unit = {}
) : CountDownTimer(millisInFuture, interval) {
    override fun onTick(millisUntilFinished: Long) {
        val secondsLeft = ((millisUntilFinished + 999) / 1000)
        countdownText.text = String.format("%02d:%02d", secondsLeft / 60, secondsLeft % 60)
        countdownText.visibility = android.view.View.VISIBLE
        onTickCallback?.invoke(secondsLeft)
    }

    override fun onFinish() {
        countdownText.text = "00:00"
        onFinishCallback()
    }
}
