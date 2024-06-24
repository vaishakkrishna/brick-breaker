package com.example.brickbreaker.data

import com.example.brickbreaker.constants.Constants
import com.example.brickbreaker.constants.Constants.brickWidth
import com.example.brickbreaker.constants.Constants.brickHeight
import com.example.brickbreaker.constants.Constants.brickSpace

data class State(
    val ball: Ball = Ball(),
    val paddle: Paddle = Paddle(),
    val bricks: Array<Array<Brick>> = Array(
        Constants.numBrickCols,
        {i -> Array(Constants.numBrickRows,
            {j -> Brick(
                Pair(i*(brickWidth + brickSpace), j*(brickHeight + brickSpace)),
                Pair(brickWidth, brickHeight)
                )
            }
            )
        }
    ),
    val gameOver: Boolean = false,
    val score: Int = 0
)

