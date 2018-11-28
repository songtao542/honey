/*
 * Copyright (c) 2018. Faraday&Future
 * All rights reserved.
 * PROPRIETARY AND CONFIDENTIAL.
 * NOTICE: All information contained herein is, and remains the property of Faraday&Future Inc.
 * The intellectual and technical concepts contained herein are proprietary to Faraday&Future Inc.
 * and may be covered by U.S. and Foreign Patents, patents in process, and are protected
 * by trade secret and copyright law. Dissemination of this code or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Faraday&Future Inc.
 * Access to the source code contained herein is hereby forbidden to anyone except current
 * Faraday&Future Inc. employees or others who have executed Confidentiality and
 * Non-disclosure agreements covering such access.
 */

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

    protected var disposeBag by autoCleared(CompositeDisposable())

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideSoftKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (onConfigureTheme()) {
            when (Sex.from(userRepository.user?.sex ?: Sex.UNKNOWN.value)) {
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
