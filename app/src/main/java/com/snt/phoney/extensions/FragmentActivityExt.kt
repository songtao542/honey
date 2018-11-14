package com.snt.phoney.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.view.inputmethod.InputMethodManager
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

inline fun FragmentActivity.setContentFragment(@IdRes containerViewId: Int, f: () -> Fragment): Fragment {
    return f().apply { supportFragmentManager?.beginTransaction()?.add(containerViewId, this)?.commit() }
}

/**
 * Method to replace the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
 */
inline fun <T : Fragment> FragmentActivity.replaceFragmentSafely(
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
    val ft = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            .replace(containerViewId, fragment, tag)
    if (addToBackStack) {
        ft.addToBackStack(backStackName ?: tag)
    }
    if (!supportFragmentManager.isStateSaved) {
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
inline fun <T : Fragment> FragmentActivity.addFragmentSafely(
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
        val ft = supportFragmentManager.beginTransaction()
                .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
                .add(containerViewId, fragment, tag)
        if (addToBackStack) {
            ft.addToBackStack(backStackName)
        }
        if (!supportFragmentManager.isStateSaved) {
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
inline fun FragmentActivity.existFragmentOfTag(tag: String): Boolean {
    return supportFragmentManager.findFragmentByTag(tag) != null
}

/**
 * Method to get fragment by tag. The operation is performed by the supportFragmentManager.
 */
inline fun FragmentActivity.findFragmentByTag(tag: String): Fragment? {
    return supportFragmentManager.findFragmentByTag(tag)
}

/**
 * Method to try to hide soft keyboard
 */
fun FragmentActivity.hideSoftKeyboard(): Boolean {
    val view = currentFocus
    view?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }
    return false
}

fun FragmentActivity.forwardOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    val fragments = supportFragmentManager.fragments
    for (fragment in fragments) {
        if (!fragment.isDetached) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}