package com.example.crowdconnectapp.components.qrcode

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun FlipCard() {
    val cardFace = remember { mutableStateOf(CardFace.Front) }

    val onClick = { clickedFace: CardFace ->
        cardFace.value = clickedFace.next
    }

    val frontContent = @Composable {
        val context = LocalContext.current
        ShowQRCode(context, "THANK YOU!", 1000)
    }

    val backContent = @Composable {

    }

    MyCardComponent(
        cardFace = cardFace.value,
        onClick = onClick,
        axis = RotationAxis.AxisY, // or AxisX depending on your preference
        back = backContent,
        front = frontContent,
        modifier = Modifier
            .scale(.7f)// Adjust as needed
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
    Card(
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
    ) {
        if (rotation.value <= 90f) {
            Box(
                Modifier.wrapContentSize()
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
                    },
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