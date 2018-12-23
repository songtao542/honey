package com.snt.phoney.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.R
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.extensions.ClearableCompositeDisposable
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.extensions.hideSoftKeyboard
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseNoViewModelActivity : AppCompatActivity(), HasSupportFragmentInjector {

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