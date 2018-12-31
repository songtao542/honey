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
import com.snt.phoney.extensions.ClearableCompositeDisposable
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.utils.KeyEventListener
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseNoViewModelActivity : AppCompatActivity(), HasSupportFragmentInjector, ActivityCompat.OnRequestPermissionsResultCallback {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var userAccessor: UserAccessor

    private var clearableDisposeBag: ClearableCompositeDisposable by autoCleared(ClearableCompositeDisposable(CompositeDisposable()))

    protected val disposeBag: CompositeDisposable
        get() = clearableDisposeBag.compositeDisposable

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    var themeId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (onConfigureTheme()) {
            when (Sex.from(userAccessor.getUser()?.sex ?: Sex.UNKNOWN.value)) {
                Sex.MALE -> {
                    themeId = R.style.AppTheme_Male
                    setTheme(themeId)
                }
                Sex.FEMALE -> {
                    themeId = R.style.AppTheme_Female
                    setTheme(R.style.AppTheme_Female)
                }
                else -> {
                    themeId = R.style.AppTheme_SexUnknown
                    setTheme(R.style.AppTheme_SexUnknown)
                }
            }
        }
    }

    open fun onConfigureTheme(): Boolean = true

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
