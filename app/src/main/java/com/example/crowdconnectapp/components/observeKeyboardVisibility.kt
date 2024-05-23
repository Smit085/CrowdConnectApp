package com.example.crowdconnectapp.components

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

@Composable
fun ObserveKeyboardVisibility(onKeyboardVisibilityChanged: (Boolean) -> Unit) {
    val rootView = LocalView.current
    val context = LocalContext.current

    DisposableEffect(context) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom
            onKeyboardVisibilityChanged(keypadHeight > screenHeight * 0.15)
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
}
