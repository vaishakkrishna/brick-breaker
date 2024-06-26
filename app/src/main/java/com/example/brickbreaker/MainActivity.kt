package com.example.brickbreaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.brickbreaker.logic.Game
import com.example.brickbreaker.ui.BrickBreaker
import com.example.brickbreaker.ui.theme.BrickBreakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val game = Game(lifecycleScope, this)
        setContent {
            BrickBreakerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BrickBreaker(game)
                }
            }
        }
    }
}