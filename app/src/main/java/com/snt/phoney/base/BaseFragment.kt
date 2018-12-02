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

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.di.Injectable
import com.snt.phoney.extensions.AndroidBug5497Workaround
import com.snt.phoney.extensions.autoCleared
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected var disposeBag by autoCleared(CompositeDisposable())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ViewCompat.requestApplyInsets(view)
    }

    fun enableOptionsMenu(toolbar: Toolbar, showTitle: Boolean = true) {
        toolbar?.let { toolbar ->
            activity?.let { activity ->
                setHasOptionsMenu(true)
                if (activity is AppCompatActivity) {
                    activity.setSupportActionBar(toolbar)
                    activity.supportActionBar?.setDisplayShowTitleEnabled(showTitle)
                }
            }
        }
    }

    companion object {
        inline fun <reified T : Fragment> newInstance(arguments: Bundle? = null) = T::class.java.newInstance().apply {
            this.arguments = arguments
        }
    }
}