package com.example.brickbreaker.data

data class Brick(
    val position: Pair<Int, Int>,
    val size: Pair<Int, Int>,
    val active: Boolean = true
)
