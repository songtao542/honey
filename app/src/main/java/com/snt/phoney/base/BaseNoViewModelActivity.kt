package com.snt.phoney.base

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.snt.phoney.R
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.utils.KeyEventListener
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseNoViewModelActivity : AppCompatActivity(), HasSupportFragmentInjector, ActivityCompat.OnRequestPermissionsResultCallback {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var userAccessor: UserAccessor

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    var themeId = 0

    private lateinit var mLoginStateHandler: LoginStateHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoginStateHandler = LoginStateHandler(this)
        val theme = onConfigureTheme()
        if (theme != null) {
            if (theme == 0) {
                when (Sex.from(userAccessor.getUser()?.sex ?: Sex.UNKNOWN.value)) {
                    Sex.MALE -> {
                        setTheme(R.style.AppTheme_Male)
                    }
                    else -> {
                        setTheme(R.style.AppTheme_Female)
                    }
                }
            } else {
                setTheme(theme)
            }
        }
        applyThemeForSex()
    }

    /**
     * 为主题追加性别相关特性
     */
    private fun applyThemeForSex() {
        when (Sex.from(userAccessor.getUser()?.sex ?: Sex.UNKNOWN.value)) {
            Sex.MALE -> {
                theme.applyStyle(R.style.Male, true)
            }
            else -> {
                theme.applyStyle(R.style.Female, true)
            }
        }
    }

    override fun setTheme(resid: Int) {
        super.setTheme(resid)
        themeId = resid
    }

    /**
     * Subclasses override this method,
     * if you don't want subclasses change the theme config in AndroidManifest.xml, you can return null
     */
    open fun onConfigureTheme(): Int? = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (dispatchKeyDownEvent(keyCode, event)) true else super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return if (dispatchKeyUpEvent(keyCode, event)) true else super.onKeyUp(keyCode, event)
    }

    private fun dispatchKeyDownEvent(keyCode: Int, event: KeyEvent): Boolean {
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment.isVisible && fragment is KeyEventListener) {
                return (fragment as KeyEventListener).onKeyDown(keyCode, event)
            }
        }
        return false
    }

    private fun dispatchKeyUpEvent(keyCode: Int, event: KeyEvent): Boolean {
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment.isVisible && fragment is KeyEventListener) {
                return (fragment as KeyEventListener).onKeyUp(keyCode, event)
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoginStateHandler.unregisterReceiver()
    }
}


/**
 * @return -1, 表示 #onConfigureTheme() 为 false
 */
@Suppress("unused")
fun Activity.getThemeId(): Int {
    return when {
        this is BaseOriginalActivity -> this.themeId
        this is BaseNoViewModelActivity -> this.themeId
        else -> -1
    }
}
