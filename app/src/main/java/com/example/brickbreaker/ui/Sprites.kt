package com.example.brickbreaker.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.brickbreaker.constants.Constants.screenHeight
import com.example.brickbreaker.constants.Constants.screenWidth
import com.example.brickbreaker.data.Ball
import com.example.brickbreaker.data.Brick
import com.example.brickbreaker.data.Paddle

fun drawBall(ball: Ball, drawScope: DrawScope, modifier: Modifier = Modifier) {
    //width scale factor
    val wsf = drawScope.size.width / screenWidth
    //height scale factor
    val hsf = drawScope.size.height / screenHeight
    // ball
    drawScope.drawCircle(
        color = Color(255, 138, 128),
        radius = ball.radius*wsf,
        center = Offset(
            ball.position.first*wsf,
            ball.position.second*hsf
        )
    )
}

fun drawPaddle(paddle: Paddle, drawScope: DrawScope, modifier: Modifier = Modifier) {
    //width scale factor
    val wsf = drawScope.size.width / screenWidth
    //height scale factor
    val hsf = drawScope.size.height / screenHeight
    drawScope.drawRect(
        color = Color(255, 138, 128),
        topLeft = Offset(
            paddle.position.first*wsf,
            paddle.position.second*hsf
        ),
        size = Size(
            paddle.size.first*wsf,
            paddle.size.second*hsf
        )
    )
}

fun drawBrick(brick: Brick, drawScope: DrawScope, modifier: Modifier = Modifier) {
    //width scale factor
    val wsf = drawScope.size.width / screenWidth
    //height scale factor
    val hsf = drawScope.size.height / screenHeight
    if (brick.active) {
        drawScope.drawRect(
            color = Color(173, 216, 230),
            topLeft = Offset(
                brick.position.first * wsf,
                brick.position.second * hsf,
            ),
            size = Size(
                brick.size.first * wsf,
                brick.size.second * hsf,
            ),
            style = Fill
        )
}
}

