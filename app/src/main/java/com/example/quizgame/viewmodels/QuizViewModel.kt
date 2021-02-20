package com.example.quizgame.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.quizgame.workers.CountdownWorker
import com.example.quizgame.workers.DefaultCountdownWorker
import kotlinx.coroutines.*

class QuizViewModel : ViewModel() {
  companion object {
    private const val START_TIME = 20
    private val TAG by lazy { QuizViewModel::class.java.simpleName }
  }

  private val countdownWorker: CountdownWorker = DefaultCountdownWorker.getInstance()

  val data: LiveData<Int>
    get() = countdownWorker.data


  fun startCountdown() {
    val onJobSucceeded: (Boolean) -> Unit = { Log.d(TAG, "Process result: $it") }
    val onJobFailed: (Throwable) -> Unit = {
      if (it is CancellationException) {
        Log.d(TAG, "Countdown job was cancelled")
      } else {
        Log.e(TAG, "Job failed: ", it)
      }
    }

    viewModelScope.launch {
      kotlin.runCatching {
        withContext(Dispatchers.Default) { countdownWorker.doWork(this, START_TIME) }
      }
        .onSuccess(onJobSucceeded)
        .onFailure(onJobFailed)
    }
  }

  fun start() = liveData {
    val emitAndWait: suspend (Int) -> Unit = { number ->
      emit(number)
      delay(1000)
      if (number == 15) throw Exception("New error")
    }
    val startProcess: suspend () -> Boolean = {
      (20 downTo 0).forEach { emitAndWait(it) }
      true
    }

    startProcess()
  }
}
