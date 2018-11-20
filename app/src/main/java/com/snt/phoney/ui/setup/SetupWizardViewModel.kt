package com.snt.phoney.ui.setup

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.model.City
import com.snt.phoney.domain.usecase.SetupWizardUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

//@ActivityScope
class SetupWizardViewModel @Inject constructor(private val application: Application, private val setupWizardUseCase: SetupWizardUseCase) : ViewModel() {

    val error = MutableLiveData<String>()
    val setupSex = MutableLiveData<String>()
    val setupFeatures = MutableLiveData<String>()

    val cities = object : LiveData<List<City>>() {
        override fun onActive() {
            postValue(setupWizardUseCase.getCities())
        }
    }

    fun getCities(): List<City> = setupWizardUseCase.getCities()

    fun setSex(sex: Int): Disposable? {
        val token = setupWizardUseCase.user?.token ?: return null
        return setupWizardUseCase.setUserSex(token, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "getCities==>$it")
                    if (it.code == 200) {
                        setupSex.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }


    fun setUserFeatures(height: Int, weight: Int, age: Int, cup: String): Disposable? {
        val token = setupWizardUseCase.user?.token ?: return null
        return setupWizardUseCase.setUserFeatures(token, height, weight, age, cup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "setUserFeatures==>$it")
                    if (it.code == 200) {
                        setupFeatures.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }


}
