package com.snt.phoney.utils

import android.content.Context
import com.snt.phoney.R
import java.text.DecimalFormat

object TimeFormat {
    fun format(context: Context, time: Long): String {
        val df = DecimalFormat.getInstance()
        val second = time / 1000
        if (second < 60) {
            return context.getString(R.string.time_second_template, df.format(second))
        }
        val minute = second / 60
        if (minute < 60) {
            return context.getString(R.string.time_minute_template, df.format(minute))
        }
        val hour = minute / 60
        if (hour < 24) {
            return context.getString(R.string.time_hour_template, df.format(hour))
        }
        val day = hour / 24
        if (day < 20) {
            return context.getString(R.string.time_day_template, df.format(day))
        }
        return ""
    }
}