package com.example.brickbreaker.logic

import com.example.brickbreaker.constants.Constants
import com.example.brickbreaker.constants.Constants.screenWidth
import com.example.brickbreaker.data.Brick
import com.example.brickbreaker.data.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class Game(private val scope: CoroutineScope) {
//    private val mutex = Mutex()
    // instantiate brick grid
    private val mutableState: MutableStateFlow<State> = MutableStateFlow(State())
    val state: Flow<State> = mutableState
    init {
        scope.launch {
            while (true) {
                delay(timeMillis = Constants.refreshDelay)
                mutableState.update {
                    var newGameOver = false;
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
                        // compute which brick is colliding with the ball
                        var brick = Brick(Pair(0, 0), Pair(0, 0))
                        for (i in 0..<it.bricks.size){
                            for (j in 0..<it.bricks[0].size) {
                                brick = it.bricks[i][j]
                                if (brick.active) {
                                    // ball hits side
                                    if (it.ball.position.second >= brick.position.second &&
                                        it.ball.position.second <= brick.position.second + brick.size.second) {
                                        // left
                                        if (it.ball.position.first + it.ball.radius >= brick.position.first &&
                                            it.ball.position.first + it.ball.radius <= brick.position.first + brick.size.first) {
                                            newBricks[i][j] = brick.copy(active=false)
                                            newVel = Pair(-1*newVel.first.absoluteValue, newVel.second)
                                            newScore++;
                                            if (it.score % 3 == 0) {
                                                newVel = Pair(newVel.first*1.1F, newVel.second*1.1F)
                                            }
                                        //right
                                        } else if (it.ball.position.first - it.ball.radius >= brick.position.first &&
                                            it.ball.position.first - it.ball.radius <= brick.position.first + brick.size.first) {
                                            newBricks[i][j] = brick.copy(active = false)
                                            newVel = Pair(newVel.first.absoluteValue, newVel.second)
                                            newScore++;
                                            if (it.score % 3 == 0) {
                                                newVel = Pair(newVel.first*1.1F, newVel.second*1.1F)
                                            }
                                        }
                                    }
                                    else if (it.ball.position.first >= brick.position.first &&
                                            it.ball.position.first <= brick.position.first + brick.size.first) {
                                            //top
                                            if (it.ball.position.second + it.ball.radius >= brick.position.second &&
                                                it.ball.position.second + it.ball.radius <= brick.position.second + brick.size.second) {
                                                newBricks[i][j] = brick.copy(active=false)
                                                newVel = Pair(newVel.first, -1*newVel.second.absoluteValue)
                                                newScore++;
                                                if (it.score % 3 == 0) {
                                                    newVel = Pair(newVel.first*1.1F, newVel.second*1.1F)
                                                }
                                            //bottom
                                            } else if (it.ball.position.second - it.ball.radius >= brick.position.second &&
                                                it.ball.position.second - it.ball.radius <= brick.position.second + brick.size.second) {
                                                newBricks[i][j] = brick.copy(active = false)
                                                newVel = Pair(newVel.first, newVel.second.absoluteValue)
                                                newScore++;
                                                if (it.score % 3 == 0) {
                                                    newVel = Pair(newVel.first*1.1F, newVel.second*1.1F)
                                                }
                                        }
                                    }
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
        println("reset")
        this.mutableState.update {
            State()
        }
    }


}