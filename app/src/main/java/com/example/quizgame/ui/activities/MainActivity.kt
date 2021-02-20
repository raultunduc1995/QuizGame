package com.example.quizgame.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityMainBinding
import com.example.quizgame.viewmodels.QuizViewModel

class MainActivity : AppCompatActivity() {

  private val viewModel by viewModels<QuizViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val setupDataBinding = {
      val binding =
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
      binding.lifecycleOwner = this
      binding.viewmodel = viewModel
    }

    setupDataBinding()
  }
}
