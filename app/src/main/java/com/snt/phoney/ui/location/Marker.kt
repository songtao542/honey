package com.snt.phoney.ui.location

import android.graphics.Bitmap
import android.location.Location

data class Marker(val location: Location, val center: Boolean, var imageResourceId: Int? = null, var imageBitmap: Bitmap? = null)