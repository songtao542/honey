package com.snt.phoney.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.R
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.Sex
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


abstract class BaseOriginalActivity : ComponentActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userAccessor: UserAccessor

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private lateinit var mLoginStateHandler: LoginStateHandler

    var themeId = -1

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

    override fun onDestroy() {
        super.onDestroy()
        mLoginStateHandler.unregisterReceiver()
    }
}
