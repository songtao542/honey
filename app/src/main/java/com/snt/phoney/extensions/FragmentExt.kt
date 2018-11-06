package com.snt.phoney.extensions

import android.os.Build
import android.util.TypedValue
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

fun Fragment.colorOf(id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context?.getColor(id) ?: resources.getColor(id)
    } else {
        resources.getColor(id)
    }
}

fun Fragment.dip(dip: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), resources.displayMetrics).toInt()
}


/**
 * Method to replace the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
 */
inline fun <T : Fragment> Fragment.replaceFragmentSafely(
        @IdRes containerViewId: Int,
        fragment: T,
        tag: String,
        addToBackStack: Boolean = false,
        backStackName: String? = null,
        allowStateLoss: Boolean = false,
        @AnimRes enterAnimation: Int = 0,
        @AnimRes exitAnimation: Int = 0,
        @AnimRes popEnterAnimation: Int = 0,
        @AnimRes popExitAnimation: Int = 0
): T {
    val ft = childFragmentManager
            .beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            .replace(containerViewId, fragment, tag)
    if (addToBackStack) {
        ft.addToBackStack(backStackName ?: tag)
    }
    if (!childFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
    return fragment
}

/**
 * Method to add the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
 * This method checks if fragment exists.
 * @return the fragment added.
 */
inline fun <T : Fragment> Fragment.addFragmentSafely(
        @IdRes containerViewId: Int,
        fragment: T,
        tag: String,
        addToBackStack: Boolean = false,
        backStackName: String? = null,
        allowStateLoss: Boolean = false,
        @AnimRes enterAnimation: Int = 0,
        @AnimRes exitAnimation: Int = 0,
        @AnimRes popEnterAnimation: Int = 0,
        @AnimRes popExitAnimation: Int = 0
): T {
    if (!existFragmentOfTag(tag)) {
        val ft = childFragmentManager.beginTransaction()
                .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
                .add(containerViewId, fragment, tag)
        if (addToBackStack) {
            ft.addToBackStack(backStackName)
        }
        if (!childFragmentManager.isStateSaved) {
            ft.commit()
        } else if (allowStateLoss) {
            ft.commitAllowingStateLoss()
        }
        return fragment
    }
    return findFragmentByTag(tag) as T
}

/**
 * Method to check if fragment exists. The operation is performed by the supportFragmentManager.
 */
inline fun Fragment.existFragmentOfTag(tag: String): Boolean {
    return childFragmentManager.findFragmentByTag(tag) != null
}

/**
 * Method to get fragment by tag. The operation is performed by the supportFragmentManager.
 */
inline fun Fragment.findFragmentByTag(tag: String): Fragment? {
    return childFragmentManager.findFragmentByTag(tag)
}
