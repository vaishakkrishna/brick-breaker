package com.example.brickbreaker.data

data class Brick(
    val position: Pair<Int, Int> = Pair(0, 0),
    val size: Pair<Int, Int> = Pair(0, 0),
    val active: Boolean = true
)
