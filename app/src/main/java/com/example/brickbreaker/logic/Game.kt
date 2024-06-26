package com.example.brickbreaker.logic

import com.example.brickbreaker.constants.Constants
import com.example.brickbreaker.constants.Constants.brickHeight
import com.example.brickbreaker.constants.Constants.brickSpace
import com.example.brickbreaker.constants.Constants.brickWidth
import com.example.brickbreaker.constants.Constants.numBrickCols
import com.example.brickbreaker.constants.Constants.numBrickRows
import com.example.brickbreaker.constants.Constants.screenWidth
import com.example.brickbreaker.data.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.math.withSign

class Game(private val scope: CoroutineScope) {
    // instantiate brick grid
    private val mutableState: MutableStateFlow<State> = MutableStateFlow(State())
    val state: Flow<State> = mutableState
    init {
        scope.launch {
            while (true) {
                delay(timeMillis = Constants.refreshDelay)
                mutableState.update {
                    var newGameOver = false
                    var newVel = Pair(it.ball.velocity.first, it.ball.velocity.second)

                    val newBricks = it.bricks.clone()
                    var newScore = it.score
                    // ball hits bottom or paddle
                    if (it.ball.position.second + it.ball.radius >= it.paddle.position.second) {
                        // ball hits paddle
                        if (it.ball.position.first > it.paddle.position.first &&
                            it.ball.position.first < it.paddle.position.first + it.paddle.size.first &&
                            it.ball.position.second <= it.paddle.position.second + it.paddle.size.second) {

                            newVel = Pair(newVel.first, min(newVel.second * -1, newVel.second))
                        }
                        // ball hits the floor, lose
                        else if (it.ball.position.second > it.paddle.position.second + it.paddle.size.second + 10){
                            newGameOver = true
                        }
                    }
                    // ball hits top
                    else if (it.ball.position.second - it.ball.radius <= 0) {
                        newVel = Pair(newVel.first, newVel.second * -1)
                    }
                    // ball hits left wall
                    else if (it.ball.position.first - it.ball.radius <= 0) {
                        newVel = Pair(max(newVel.first * -1, newVel.first), newVel.second)
                    }
                    // ball hits right wall
                    else if (it.ball.position.first + it.ball.radius >= screenWidth) {
                        newVel = Pair(min(newVel.first, newVel.first*-1), newVel.second)
                    }
                    else {
                        // compute which bricks are colliding with the ball on
                        // each side (top, bottom, right, left) of the ball
                        val directions = arrayOf(
                            // left
                            Pair(-1*it.ball.radius, 0F),
                            // right
                            Pair(it.ball.radius, 0F),
                            // top
                            Pair(0F, -1*it.ball.radius),
                            // bottom
                            Pair(0F, it.ball.radius),
                        )
                        var coord: Pair<Float, Float>
                        var col: Int
                        var row: Int
                        for (d in directions.indices) {
                            coord = Pair(
                                it.ball.position.first + directions[d].first,
                                it.ball.position.second + directions[d].second
                            )
                            row = coord.second.toInt() / (brickHeight + brickSpace)
                            col = coord.first.toInt() / (brickWidth + brickSpace)

                            println("coord (${coord.first}, ${coord.second}) row ${row} col ${col}")
                            if (row < numBrickRows &&
                                col < numBrickCols &&
                                newBricks[row][col].active) {
                                newBricks[row][col] = it.bricks[row][col].copy(active=false)
                                newVel = Pair(
                                    newVel.first.withSign(-directions[d].first),
                                    newVel.second.withSign(-directions[d].second)
                                )
                                newScore++
                                if (it.score % 3 == 0) {
                                    newVel = Pair(newVel.first*1.1F, newVel.second*1.1F)
                                }
                            }
                        }
                    }
                    val newPos = Pair(
                        it.ball.position.first + newVel.first,
                        it.ball.position.second + newVel.second
                    )
                    it.copy(
                        ball = it.ball.copy(position = newPos, velocity = newVel),
                        bricks = newBricks,
                        gameOver = newGameOver,
                        score = newScore
                    )

                }
            }
        }
    }

    fun handlePaddleMove(xPos: Int) {
        this.mutableState.update {
            val newXPos = min(
                max(0, xPos-it.paddle.size.first/2),
                screenWidth-it.paddle.size.first
            )
            it.copy(
                paddle = it.paddle.copy(
                    position = Pair(newXPos, it.paddle.position.second)
                )
            )
        }

    }

    fun reset() {
        this.mutableState.update {
            State()
        }
    }


}