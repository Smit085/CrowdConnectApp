package com.example.crowdconnectapp.components.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.number.Scale
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

fun generateQRCode(context: Context, qrCodeData: String, size: Int): Bitmap? {
    val hints = mutableMapOf<EncodeHintType, Any>()
    hints[EncodeHintType.MARGIN] = 2
    try {
        val matrix = MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, size, size, hints)
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (matrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    } catch (e: WriterException) {
        e.printStackTrace()
    }
    return null
}

@Composable
fun ShowQRCode(context: Context, qrCodeData: String, size: Int) {
    Box(){
    val qrCodeBitmap = generateQRCode(context, qrCodeData, size)
    qrCodeBitmap?.let {
        Image(modifier = Modifier
            .fillMaxWidth()
            .scale(0.8f)
            .wrapContentSize(), contentScale = ContentScale.Fit, bitmap = it.asImageBitmap(), contentDescription = null)
    }
    }
}

