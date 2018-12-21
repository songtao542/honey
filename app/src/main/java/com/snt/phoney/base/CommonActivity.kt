package com.snt.phoney.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import com.snt.phoney.R
import com.snt.phoney.extensions.*

const val EXTRA_ARGUMENT = "argument"
const val EXTRA_PAGE = "page"

open class CommonActivity : BaseActivity() {

    companion object {
        @JvmStatic
        inline fun <reified T : Activity> newIntent(context: Context, page: Page, argument: Bundle? = null): Intent {
            val intent = BaseActivity.newIntent(context, T::class.java, page)
            argument?.let {
                intent.putExtra(EXTRA_ARGUMENT, argument)
            }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()
        setContentView(R.layout.activity_common)
        setStatusBarColor(colorOf(android.R.color.white))
        if (savedInstanceState == null) {
            val page = intent?.getIntExtra(EXTRA_PAGE, 1) ?: -1
            val argument = intent?.getBundleExtra(EXTRA_ARGUMENT)
            val fragment = FragmentFactory.create(page, argument)
            addFragmentSafely(R.id.container, fragment, fragment::class.simpleName!!)
        }
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
    return if (this is CommonActivity) {
        this.addFragmentSafely(R.id.container, fragment, tag, addToBackStack, backStackName, allowStateLoss, enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
    } else {
        fragment
    }
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
    return if (this is CommonActivity) {
        argument?.let { args ->
            if (fragment.arguments != null) {
                fragment.arguments!!.putAll(args)
            } else {
                fragment.arguments = args
            }
        }
        this.addFragmentSafely(R.id.container, fragment, tag, addToBackStack, backStackName, allowStateLoss, enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
    } else {
        fragment
    }
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
    return if (this is CommonActivity) {
        this.replaceFragmentSafely(R.id.container, fragment, tag, addToBackStack, backStackName, allowStateLoss, enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
    } else {
        fragment
    }
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
    return if (this is CommonActivity) {
        argument?.let { args ->
            if (fragment.arguments != null) {
                fragment.arguments!!.putAll(args)
            } else {
                fragment.arguments = args
            }
        }
        this.replaceFragmentSafely(R.id.container, fragment, tag, addToBackStack, backStackName, allowStateLoss, enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
    } else {
        fragment
    }
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

