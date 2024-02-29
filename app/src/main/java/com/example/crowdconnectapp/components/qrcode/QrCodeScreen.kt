package com.example.crowdconnectapp.components.qrcode

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun QrcodeScreen() {
    val pagerState = rememberPagerState(pageCount = { 2 })
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
    ) {
        val context = LocalContext.current
        if (it % 2 == 0) {
            // Show QR code
            ShowQRCode(context, "THANK YOU", 1000)
        } else {
            // Show manual code display
            Text(
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center,
                text = "Manual Code"
            )
        }
    }
}
