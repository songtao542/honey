package com.snt.phoney.extensions

import android.graphics.*


val CROP_PAINT: Paint = Paint(Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
val CIRCLE_CROP_BITMAP_PAINT = Paint(Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG).apply {
    xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
}

inline fun Bitmap.circle(): Bitmap {
    val radius = if (width > height) (height / 2).toFloat() else (width / 2).toFloat()
    val result = Bitmap.createBitmap((radius * 2).toInt(), (radius * 2).toInt(), Bitmap.Config.ARGB_8888)
    result.setHasAlpha(true)

    val srcWidth = width
    val srcHeight = height
    val destMinEdge = radius * 2
    val scaleX = destMinEdge / srcWidth.toFloat()
    val scaleY = destMinEdge / srcHeight.toFloat()
    val maxScale = Math.max(scaleX, scaleY)

    val scaledWidth = maxScale * srcWidth
    val scaledHeight = maxScale * srcHeight
    val left = (destMinEdge - scaledWidth) / 2f
    val top = (destMinEdge - scaledHeight) / 2f
    val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
    val paint = Paint()
    paint.isAntiAlias = true
    try {
        val canvas = Canvas(result)
        // Draw a circle
        canvas.drawCircle(radius, radius, radius, CROP_PAINT)
        // Draw the bitmap in the circle
        canvas.drawBitmap(this, null, destRect, CIRCLE_CROP_BITMAP_PAINT)
        canvas.setBitmap(null)
    } finally {

    }
    return result
}