package com.snt.phoney.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.R
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.domain.repository.UserRepository
import com.snt.phoney.extensions.ClearableCompositeDisposable
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.extensions.hideSoftKeyboard
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


abstract class BaseOriginalActivity : ComponentActivity(), HasSupportFragmentInjector {
    companion object {
        fun <T : Activity> newIntent(context: Context, clazz: Class<T>, page: Page): Intent {
            val intent = Intent(context, clazz)
            return intent.putExtra(EXTRA_PAGE, page.ordinal)
        }
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userRepository: UserRepository

    private var clearableDisposeBag: ClearableCompositeDisposable by autoCleared(ClearableCompositeDisposable(CompositeDisposable()))

    protected val disposeBag: CompositeDisposable
        get() = clearableDisposeBag.compositeDisposable

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideSoftKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (onConfigureTheme()) {
            when (Sex.from(userRepository.getUser()?.sex ?: Sex.UNKNOWN.value)) {
                Sex.MALE -> {
                    setTheme(R.style.AppTheme_Male)
                }
                Sex.FEMALE -> {
                    setTheme(R.style.AppTheme_Female)
                }
                else -> {
                    setTheme(R.style.AppTheme_SexUnknown)
                }
            }
        }
    }

    open fun onConfigureTheme(): Boolean = true
}
