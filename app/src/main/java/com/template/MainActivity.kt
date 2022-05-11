package com.template

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.template.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val icon = binding.imgSplash
        val animation: Animation = AlphaAnimation(0.5f, 1.0f)
        animation.duration = 700
        animation.startOffset = 50
        animation.repeatMode = Animation.REVERSE
        animation.repeatCount = Animation.INFINITE
        icon.startAnimation(animation)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            next()
        }
    }

    private suspend fun next() {
        delay(1500)
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}