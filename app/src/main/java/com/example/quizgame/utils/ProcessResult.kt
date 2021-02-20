package com.example.quizgame.utils

sealed class ProcessResult<out T>(
  val data: T? = null,
  val message: String? = null
) {
  data class Success<T>(val result: T) : ProcessResult<T>(data = result)
  data class Failed<T>(val reason: String?) : ProcessResult<T>(message = reason)
}
