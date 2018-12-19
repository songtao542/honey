package com.snt.phoney.utils.life

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import com.snt.phoney.base.ComponentActivity


object ViewModelProviders {
    fun <T : ViewModel> of(activity: Activity, modelClass: Class<T>): T {
        return of(activity, null, modelClass)
    }

    fun <T : ViewModel> of(activity: Activity, factory: Factory? = null, modelClass: Class<T>): T {
        return when (activity) {
            is FragmentActivity -> androidx.lifecycle.ViewModelProviders.of(activity, factory).get(modelClass)
            is ComponentActivity -> {
                val application = activity.application
                        ?: throw   IllegalStateException("Your activity/fragment is not yet attached to Application. You can't request AppViewModel before onCreate call.")
                ViewModelProvider(activity.viewModelStore, factory
                        ?: ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(modelClass)
            }
            else -> throw IllegalArgumentException("The activity argument must be type of FragmentActivity or ComponentActivity")
        }
    }

    fun <T : ViewModel> of(fragment: Fragment, factory: Factory? = null, modelClass: Class<T>): T {
        return androidx.lifecycle.ViewModelProviders.of(fragment, factory).get(modelClass)
    }
}