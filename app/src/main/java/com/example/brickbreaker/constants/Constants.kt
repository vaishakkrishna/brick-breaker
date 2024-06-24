package com.example.brickbreaker.constants

object Constants {
    val refreshDelay: Long = 15
    val numBrickRows: Int = 9
    val numBrickCols: Int = 6
    val brickHeight: Int = 50
    val brickSpace: Int = 15
    val screenWidth: Int = 1000
    val screenHeight: Int = 1900
    val brickWidth: Int = screenWidth/(numBrickCols).toInt() - brickSpace
}