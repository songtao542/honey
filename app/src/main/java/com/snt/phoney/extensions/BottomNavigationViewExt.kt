package com.snt.phoney.extensions

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.ViewGroup

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
    postInvalidate()
} catch (exception: Exception) {
    Log.e("BottomNavigationView", exception.message)
}