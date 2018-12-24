package com.snt.phoney.utils

import android.view.View
import androidx.databinding.BindingConversion
import java.text.DecimalFormat


object BindingAdapters {

//    @BindingAdapter("app:url")
//    @JvmStatic
//    fun url(view: ImageView, url: String) {
//        Glide.with(view).load(url).into(view)
//    }

}

object BindingConverters {

    @BindingConversion
    @JvmStatic
    fun booleanToVisibility(isNotVisible: Boolean): Int {
        return if (isNotVisible) View.GONE else View.VISIBLE
    }
}

object ConverterUtil {
    @JvmStatic
    fun toString(number: Int): String {
        return "$number"
    }

    @JvmStatic
    fun toString(number: Double): String {
        val df = DecimalFormat.getInstance()
        return df.format(number)
    }

    @JvmStatic
    fun toString(number: Float): String {
        val df = DecimalFormat.getInstance()
        return df.format(number)
    }
}
