package com.example.quizgame.workers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

interface CountdownWorker {
  val data: LiveData<Int>
  suspend fun doWork(coroutineScope: CoroutineScope, startTime: Int): Boolean
}

class DefaultCountdownWorker : CountdownWorker {
  companion object {
    private val TAG by lazy { DefaultCountdownWorker::class.java.simpleName }

    fun getInstance(): CountdownWorker = DefaultCountdownWorker()
  }

  override val data: MutableLiveData<Int> = MutableLiveData()
  //    val data: LiveData<Int> = liveData {
  //        val emitAndWait: suspend (Int) -> Unit = { number -> emit(number); delay(1000) }
  //
  //        (20 downTo  0).forEach { emitAndWait(it) }
  //    }

  private suspend fun secondJob(): String {
    val generateText: suspend (String, Int) -> String = { text, toAppend ->
      val newText = text + toAppend.toString()
      delay(2000)
      Log.d(TAG, "New text: $newText")
      newText
    }
    val doJob: suspend () -> String = {
      var text = ""
      (0..10).forEach { text = generateText(text, it) }
      text
    }

    return doJob()
  }

  private suspend fun firstJob(startTime: Int): Boolean {
    val emitAndWait: suspend (Int) -> Unit = { number ->
      data.postValue(number)
      delay(1000)
//      if (number == 15) throw Exception("New error")
    }
    val startProcess: suspend () -> Boolean = {
      (startTime downTo 0).forEach { emitAndWait(it) }
      true
    }

    return startProcess()
  }

  override suspend fun doWork(coroutineScope: CoroutineScope, startTime: Int): Boolean {
    val jobs = listOf(
      coroutineScope.async { firstJob(startTime) }
    )

    Log.d(TAG, "Jobs result: ${jobs.awaitAll()}")
    return true
  }
}