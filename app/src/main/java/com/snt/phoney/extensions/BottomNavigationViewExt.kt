package com.snt.phoney.extensions

import android.annotation.SuppressLint
import android.support.design.R
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() = try {
    var shiftingModeOfMenuView = BottomNavigationMenuView::class.java.getDeclaredField("mShiftingMode")
    shiftingModeOfMenuView.isAccessible = true
    for (menu in this.asSequence()) {
        if (menu is BottomNavigationMenuView) {
            shiftingModeOfMenuView.setBoolean(menu, false)
            for (item in menu.asSequence()) {
                if (item is BottomNavigationItemView) {
                    item.setShiftingMode(false)
                    item.setChecked(item.itemData.isChecked)
                }
            }
        }
    }
    shiftingModeOfMenuView.isAccessible = false
} catch (exception: Exception) {
    Log.e("BottomNavigationView", exception.message)
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.hideIcon() = try {
    var icon = BottomNavigationItemView::class.java.getDeclaredField("mIcon")
    icon.isAccessible = true
    for (menu in this.asSequence()) {
        if (menu is BottomNavigationMenuView) {
            for (item in menu.asSequence()) {
                if (item is BottomNavigationItemView) {
                    item.findViewById<View>(R.id.icon).visibility = View.GONE
                    var label = item.getChildAt(1)
                    var lp = label.layoutParams as FrameLayout.LayoutParams
                    lp.gravity = Gravity.CENTER
                    label.layoutParams = lp
                    label.setPadding(0, 0, 0, 0)
                    label.findViewById<TextView>(R.id.smallLabel).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    label.findViewById<TextView>(R.id.largeLabel).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                }

            }
        }
    }
    icon.isAccessible = false
} catch (exception: Exception) {
    Log.e("BottomNavigationView", exception.message)
}