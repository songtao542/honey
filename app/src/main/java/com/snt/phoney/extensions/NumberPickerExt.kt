package com.snt.phoney.extensions

import android.graphics.drawable.ColorDrawable
import android.widget.NumberPicker
import java.lang.reflect.Field

/**
 * 设置picker分割线的颜色
 */
fun NumberPicker.setDividerColor(color: Int) {
    val field: Field = NumberPicker::class.java.getDeclaredField("mSelectionDivider")
    if (field != null) {
        field.isAccessible = true
        field.set(this, ColorDrawable(color))
    }
}

/**
 * 设置picker分割线的宽度
 */
fun NumberPicker.setDividerHeight(height: Int) {
    val fields = NumberPicker::class.java.declaredFields
    for (field in fields) {
        if (field.name == "mSelectionDividerHeight") {
            field.isAccessible = true
            field.set(this, dip(height))
            break
        }
    }
}
