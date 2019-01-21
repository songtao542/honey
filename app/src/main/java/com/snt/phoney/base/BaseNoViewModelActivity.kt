package com.snt.phoney.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.snt.phoney.R
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.utils.KeyEventListener
import com.umeng.analytics.MobclickAgent
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

    private var mLoginStateHandler: LoginStateHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onConfigureTheme()
    }

    open fun onConfigureTheme() {
        val sex = Sex.from(userAccessor.getUser()?.sex ?: Sex.UNKNOWN.value)
        val themeId = if (sex == Sex.MALE) R.style.AppTheme_Male else R.style.AppTheme_Female
        val newThemeId = onApplyTheme(themeId) // onApplyTheme() 默认返回 themeId
        //子类返回的 newThemeId 如果不等于 0, 表示子类需要配置 theme
        //子类返回的 newThemeId 如果等于 0, 表示子类不希望覆盖 AndroidManifest.xml 中配置的theme, 所以等于0时, 不调用 setTheme() 方法
        if (newThemeId != 0) {
            setTheme(newThemeId)
        }
        applyThemeForSex(sex)
    }

    /**
     * Subclasses override this method,
     * if you don't want subclasses change the theme config in AndroidManifest.xml, you can return 0
     */
    open fun onApplyTheme(themeId: Int): Int = themeId

    /**
     * 为主题追加性别相关特性
     */
    private fun applyThemeForSex(sex: Sex) {
        when (sex) {
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

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

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

    open fun shouldObserveLoginState(): Boolean = true

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (shouldObserveLoginState()) {
            mLoginStateHandler = LoginStateHandler(this)
        }
    }

    override fun onDestroy() {
        mLoginStateHandler?.unregisterReceiver()
        super.onDestroy()
    }
}


/**
 * @return -1, 表示 theme 未知
 */
@Suppress("unused")
fun Activity.getThemeId(): Int {
    return when {
        this is BaseNoViewModelActivity -> this.themeId
        else -> -1
    }
}
