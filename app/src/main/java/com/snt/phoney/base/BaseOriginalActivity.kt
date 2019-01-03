package com.snt.phoney.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.R
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.extensions.ClearableCompositeDisposable
import com.snt.phoney.extensions.autoCleared
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


abstract class BaseOriginalActivity : ComponentActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userAccessor: UserAccessor

    private var clearableDisposeBag: ClearableCompositeDisposable by autoCleared(ClearableCompositeDisposable(CompositeDisposable()))

    protected val disposeBag: CompositeDisposable
        get() = clearableDisposeBag.compositeDisposable

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    var themeId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val theme = onConfigureTheme()
        if (theme != null) {
            if (theme == 0) {
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
            } else {
                themeId = theme
                setTheme(theme)
            }
        }
    }

    /**
     * Subclasses override this method,
     * if you don't want subclasses change the theme config in AndroidManifest.xml, you can return null
     */
    open fun onConfigureTheme(): Int? = 0
}
