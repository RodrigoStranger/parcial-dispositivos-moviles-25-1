package com.example.juegodecolores

import android.app.Application
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private var _tiempoRestante = MutableLiveData<Long>()
    val tiempoRestante: LiveData<Long> get() = _tiempoRestante

    private var _timerRunning = MutableLiveData<Boolean>()
    val timerRunning: LiveData<Boolean> get() = _timerRunning

    private var gameTimer: CountDownTimer? = null
    private var backgroundPlayer: MediaPlayer? = null
    private var pausaTimestamp: Long = 0L
    private var tiempoGuardado: Long = 0L

    fun startTimer(tiempo: Long) {
        _timerRunning.value = true
        _tiempoRestante.value = tiempo
        gameTimer?.cancel()
        gameTimer = object : CountDownTimer(tiempo, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                _tiempoRestante.value = millisUntilFinished
            }
            override fun onFinish() {
                _tiempoRestante.value = 0L
                _timerRunning.value = false
            }
        }
        gameTimer?.start()
    }

    fun pauseGame() {
        gameTimer?.cancel()
        tiempoGuardado = _tiempoRestante.value ?: 0L
        pausaTimestamp = System.currentTimeMillis()
        backgroundPlayer?.pause()
        _timerRunning.value = false
    }

    fun resumeGame() {
        if (tiempoGuardado > 0) {
            startTimer(tiempoGuardado)
        }
        backgroundPlayer?.start()
    }

    fun setMediaPlayer(player: MediaPlayer?) {
        backgroundPlayer = player
    }

    override fun onCleared() {
        super.onCleared()
        gameTimer?.cancel()
        backgroundPlayer?.release()
    }
}
