package com.example.brickbreaker.data


data class Paddle(
    val position: Pair<Int, Int> = Pair(10, 1600),
    val size: Pair<Int, Int> = Pair(200, 20)
)
