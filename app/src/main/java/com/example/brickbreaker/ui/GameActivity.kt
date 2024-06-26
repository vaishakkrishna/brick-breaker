package com.example.brickbreaker.ui
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brickbreaker.constants.Constants.screenHeight
import com.example.brickbreaker.constants.Constants.screenWidth
import com.example.brickbreaker.data.State
import com.example.brickbreaker.logic.Game
import com.example.brickbreaker.ui.theme.BrickBreakerTheme


@SuppressLint("ReturnFromAwaitPointerEventScope")
@Composable
fun BrickBreaker(game: Game) {
    val state = game.state.collectAsState(initial = null)
    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Box(Modifier
            .size(
                width = maxWidth,
                height = maxHeight
            )
            .pointerInput(PointerEventType.Move) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        // handle pointer event
                        if (event.type == PointerEventType.Move) {
                            game.handlePaddleMove(event.changes.first().position.x.toInt())
                        }
                    }
                }
            }
            .pointerInput(PointerEventType.Press) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        // handle pointer event
                        if (event.type == PointerEventType.Press) {
                            game.handlePaddleMove(event.changes.first().position.x.toInt())
                        }
                    }
                }
            }) {
            state.value?.let {
                if (it.gameOver) {
                    GameOver({game.reset()}, it.score)
                } else {
                    PlayArea(it, modifier = Modifier
                        .background(color = Color.Black))
                }
            }
        }
    }
}

@Composable
fun PlayArea(state: State, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(
                top = 40.dp,
                bottom = 20.dp
            )
    ) {
        ScoreCard(score = state.score)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .border(border = BorderStroke(2.dp, Color.Black))

        ) {
            drawBall(state.ball, this)
            drawPaddle(state.paddle, this)
            state.bricks.forEach { brickRow ->
                brickRow.forEach { brick ->
                    drawBrick(brick, this)
                }
            }
        }
    }
}
@Composable
fun ScoreCard(score: Int, modifier: Modifier = Modifier) {
    Text(
        text = "Score: ${score}",
        modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .border(BorderStroke(4.dp, Color.Gray)),
        fontSize = 30.sp,
        color = Color.Black
    )
}
@Composable
fun GameOver(resetCallback: () -> Unit, score: Int, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxHeight()
    ) {
        Text(text = "Game Over!", fontSize = 100.sp, lineHeight = 100.sp, textAlign=TextAlign.Center)
        Text(text = "Score: $score", fontSize = 80.sp, lineHeight = 100.sp, textAlign=TextAlign.Center)
        Button(onClick = {resetCallback()}) {
            Text(text = "Play Again")

        }
    }

}


@Preview(showBackground = true)
@Composable
fun PlayAreaPreview() {
    BrickBreakerTheme {
        PlayArea(state = State())
    }
}

