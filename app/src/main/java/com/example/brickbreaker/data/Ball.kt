package com.example.brickbreaker.data

data class Ball(
    // position on the canvas
    val position: Pair<Float, Float> = Pair(300F, 1000F),
    // radius
    val radius: Float = 15F,
    // pixels per second
    val velocity: Pair<Float, Float> = Pair(6F, 6F)
)

