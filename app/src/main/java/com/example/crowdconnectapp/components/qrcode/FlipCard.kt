package com.example.crowdconnectapp.components.qrcode

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.crowdconnectapp.models.QuizViewModel
import com.example.crowdconnectapp.ui.theme.VividBlue
import com.example.crowdconnectapp.ui.theme.VividBlue50

@Composable
fun FlipCard() {
    val quizViewModel: QuizViewModel = hiltViewModel()
    val cardFace = remember { mutableStateOf(CardFace.Front) }

    val onClick = { clickedFace: CardFace ->
        cardFace.value = clickedFace.next
    }

    val frontContent = @Composable {
        val context = LocalContext.current
        ShowQRCode(context, quizViewModel.sessioncode, 1000)
    }

    val backContent = @Composable {
        Text(text = quizViewModel.sessioncode, style = MaterialTheme.typography.displayLarge, color = Color.White)
    }

    MyCardComponent(
        cardFace = cardFace.value,
        onClick = onClick,
        axis = RotationAxis.AxisY,
        back = backContent,
        front = frontContent,
        modifier = Modifier
            .scale(.7f)
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCardComponent(
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    modifier: Modifier = Modifier,
    axis: RotationAxis = RotationAxis.AxisY,
    back: @Composable () -> Unit = {},
    front: @Composable () -> Unit = {},
) {
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ), label = ""
    )
    ElevatedCard(
        onClick = { onClick(cardFace) },
        modifier = modifier
            .graphicsLayer {
                if (axis == RotationAxis.AxisX) {
                    rotationX = rotation.value
                } else {
                    rotationY = rotation.value
                }
                cameraDistance = 12f * density
            }
            .shadow(
                elevation = 40.dp,
                spotColor = Color.Blue,
                shape = RoundedCornerShape(10.dp)
            ),elevation = CardDefaults.cardElevation(
            defaultElevation = 56.dp
        ),
    ) {
        if (rotation.value <= 90f) {
            Box(
                Modifier
                .size(400.dp)
                    .background(VividBlue50)
            ) {
                front()
            }
        } else {
            Box(
                Modifier
                    .size(400.dp)
                    .graphicsLayer {
                        if (axis == RotationAxis.AxisX) {
                            rotationX = 180f
                        } else {
                            rotationY = 180f
                        }
                    }
                    .background(VividBlue50),
                contentAlignment = Alignment.Center
            ) {
                back()
            }
        }
    }
}
enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

enum class RotationAxis {
    AxisX,
    AxisY,
}