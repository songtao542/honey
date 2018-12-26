package com.snt.phoney.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import com.snt.phoney.R
import com.snt.phoney.base.*

inline fun <reified T : Activity> Context.newIntent(page: Page, argument: Bundle? = null): Intent {
    val intent = Intent(this, T::class.java)
    intent.putExtra(EXTRA_PAGE, page.ordinal)
    argument?.let {
        intent.putExtra(EXTRA_ARGUMENT, argument)
    }
    return intent
}

inline fun <reified T : Activity> Context.startActivity(page: Page, argument: Bundle? = null) {
    startActivity(newIntent<T>(page, argument))
}

inline fun <reified T : Activity> Activity.startActivityForResult(page: Page, requestCode: Int, argument: Bundle? = null) {
    startActivityForResult(newIntent<T>(page, argument), requestCode)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(page: Page, requestCode: Int, argument: Bundle? = null) {
    context?.let { context ->
        startActivityForResult(context.newIntent<T>(page, argument), requestCode)
    }
}

inline fun <reified T : Activity> Fragment.startActivity(page: Page, argument: Bundle? = null) {
    context?.let { context ->
        startActivity(context.newIntent<T>(page, argument))
    }
}


inline fun Activity.addFragmentSafely(page: Page,
                                      tag: String,
                                      addToBackStack: Boolean = false,
                                      backStackName: String? = null,
                                      allowStateLoss: Boolean = false,
                                      argument: Bundle? = null,
                                      @AnimRes enterAnimation: Int = 0,
                                      @AnimRes exitAnimation: Int = 0,
                                      @AnimRes popEnterAnimation: Int = 0,
                                      @AnimRes popExitAnimation: Int = 0): Fragment {
    val fragment = FragmentFactory.create(page.ordinal, argument)
    val containerId = if (this is CommonNoViewModelActivity) R.id.container else android.R.id.content
    return this.addFragmentSafely(containerId, fragment, tag, addToBackStack, backStackName, allowStateLoss,
            enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
}

inline fun Activity.addFragmentSafely(fragment: Fragment,
                                      tag: String,
                                      addToBackStack: Boolean = false,
                                      backStackName: String? = null,
                                      allowStateLoss: Boolean = false,
                                      argument: Bundle? = null,
                                      @AnimRes enterAnimation: Int = 0,
                                      @AnimRes exitAnimation: Int = 0,
                                      @AnimRes popEnterAnimation: Int = 0,
                                      @AnimRes popExitAnimation: Int = 0): Fragment {

    argument?.let { args ->
        if (fragment.arguments != null) {
            fragment.arguments!!.putAll(args)
        } else {
            fragment.arguments = args
        }
    }
    val containerId = if (this is CommonNoViewModelActivity) R.id.container else android.R.id.content
    return this.addFragmentSafely(containerId, fragment, tag, addToBackStack, backStackName, allowStateLoss,
            enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
}

inline fun Activity.replaceFragmentSafely(page: Page,
                                          tag: String,
                                          addToBackStack: Boolean = false,
                                          backStackName: String? = null,
                                          allowStateLoss: Boolean = false,
                                          argument: Bundle? = null,
                                          @AnimRes enterAnimation: Int = 0,
                                          @AnimRes exitAnimation: Int = 0,
                                          @AnimRes popEnterAnimation: Int = 0,
                                          @AnimRes popExitAnimation: Int = 0): Fragment {
    val fragment = FragmentFactory.create(page.ordinal, argument)
    val containerId = if (this is CommonNoViewModelActivity) R.id.container else android.R.id.content
    return this.replaceFragmentSafely(containerId, fragment, tag, addToBackStack, backStackName, allowStateLoss,
            enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
}

inline fun <T : Fragment> Activity.replaceFragmentSafely(
        fragment: T,
        tag: String,
        addToBackStack: Boolean = false,
        backStackName: String? = null,
        allowStateLoss: Boolean = false,
        argument: Bundle? = null,
        @AnimRes enterAnimation: Int = 0,
        @AnimRes exitAnimation: Int = 0,
        @AnimRes popEnterAnimation: Int = 0,
        @AnimRes popExitAnimation: Int = 0): T {
    argument?.let { args ->
        if (fragment.arguments != null) {
            fragment.arguments!!.putAll(args)
        } else {
            fragment.arguments = args
        }
    }
    val containerId = if (this is CommonNoViewModelActivity) R.id.container else android.R.id.content
    return this.replaceFragmentSafely(containerId, fragment, tag, addToBackStack, backStackName, allowStateLoss,
            enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
}

inline fun Fragment.addFragmentSafely(page: Page,
                                      tag: String,
                                      addToBackStack: Boolean = false,
                                      backStackName: String? = null,
                                      allowStateLoss: Boolean = false,
                                      argument: Bundle? = null,
                                      @AnimRes enterAnimation: Int = 0,
                                      @AnimRes exitAnimation: Int = 0,
                                      @AnimRes popEnterAnimation: Int = 0,
                                      @AnimRes popExitAnimation: Int = 0): Fragment {
    val fragment = FragmentFactory.create(page.ordinal, argument)
    return activity?.addFragmentSafely(fragment, tag, addToBackStack,
            backStackName, allowStateLoss, argument,
            enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation) ?: fragment
}

inline fun Fragment.addFragmentSafely(fragment: Fragment,
                                      tag: String,
                                      addToBackStack: Boolean = false,
                                      backStackName: String? = null,
                                      allowStateLoss: Boolean = false,
                                      argument: Bundle? = null,
                                      @AnimRes enterAnimation: Int = 0,
                                      @AnimRes exitAnimation: Int = 0,
                                      @AnimRes popEnterAnimation: Int = 0,
                                      @AnimRes popExitAnimation: Int = 0): Fragment {
    return activity?.addFragmentSafely(fragment, tag, addToBackStack,
            backStackName, allowStateLoss, argument,
            enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation) ?: fragment
}


inline fun Fragment.replaceFragmentSafely(page: Page,
                                          tag: String,
                                          addToBackStack: Boolean = false,
                                          backStackName: String? = null,
                                          allowStateLoss: Boolean = false,
                                          argument: Bundle? = null,
                                          @AnimRes enterAnimation: Int = 0,
                                          @AnimRes exitAnimation: Int = 0,
                                          @AnimRes popEnterAnimation: Int = 0,
                                          @AnimRes popExitAnimation: Int = 0): Fragment {
    val fragment = FragmentFactory.create(page.ordinal, argument)
    return activity?.replaceFragmentSafely(fragment, tag, addToBackStack,
            backStackName, allowStateLoss, argument,
            enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation) ?: fragment
}

inline fun <T : Fragment> Fragment.replaceFragmentSafely(
        fragment: T,
        tag: String,
        addToBackStack: Boolean = false,
        backStackName: String? = null,
        allowStateLoss: Boolean = false,
        argument: Bundle? = null,
        @AnimRes enterAnimation: Int = 0,
        @AnimRes exitAnimation: Int = 0,
        @AnimRes popEnterAnimation: Int = 0,
        @AnimRes popExitAnimation: Int = 0): T {
    return activity?.replaceFragmentSafely(fragment, tag, addToBackStack,
            backStackName, allowStateLoss, argument,
            enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation) ?: fragment
}