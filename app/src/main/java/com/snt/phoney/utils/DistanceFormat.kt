package com.snt.phoney.utils

import android.content.Context
import com.snt.phoney.R
import java.text.DecimalFormat

object DistanceFormat {
    fun format(context: Context, distance: Double): String {
        return if (distance > 1000) {
            DecimalFormat("##.#").format(distance / 1000) + context.getString(R.string.unit_km)
        } else {
            DecimalFormat("##").format(distance) + context.getString(R.string.unit_m)
        }
    }
}