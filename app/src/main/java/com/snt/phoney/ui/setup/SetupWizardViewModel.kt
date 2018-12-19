package com.snt.phoney.ui.setup

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Career
import com.snt.phoney.domain.model.City
import com.snt.phoney.domain.model.CityPickerConverter
import com.snt.phoney.domain.model.Purpose
import com.snt.phoney.domain.usecase.SetupWizardUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetupWizardViewModel @Inject constructor(private val setupWizardUseCase: SetupWizardUseCase) : AppViewModel() {

    val error = MutableLiveData<String>()
    val setupSex = MutableLiveData<String>()
    val setupFeatures = MutableLiveData<String>()
    val setupUserInfo = MutableLiveData<String>()

    val cities = object : LiveData<List<com.zaaach.citypicker.model.City>>() {
        override fun onActive() {
            GlobalScope.launch(Dispatchers.Default) {
                postValue(CityPickerConverter.convert(setupWizardUseCase.getCities()))
            }
        }
    }

    val careers = object : LiveData<List<Career>>() {
        override fun onActive() {
            setupWizardUseCase.user?.token?.let { token ->
                setupWizardUseCase.listCareer(token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy {
                            value = it.data
                        }
            }
        }
    }

    val purposes = object : LiveData<List<Purpose>>() {
        override fun onActive() {
            setupWizardUseCase.user?.token?.let { token ->
                setupWizardUseCase.listPurpose(token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy {
                            value = it.data
                        }
            }
        }
    }


    fun setSex(sex: Int): Disposable? {
        val token = setupWizardUseCase.user?.token ?: return null
        return setupWizardUseCase.setUserSex(token, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "getCities==>$it")
                    if (it.code == 200) {
                        setupSex.value = it.data
                        setupWizardUseCase.user?.sex = sex
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
                        val user = setupWizardUseCase.user?.copy(height = height, weight = weight.toDouble(), age = age, cup = cup)
                        if (user != null) {
                            setupWizardUseCase.user = user
                        }
                        setupFeatures.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }

    /**
     * @param cities,城市列表id用逗号分隔
     * @param career,职业 对于文字，接口返回
     * @param program,宣言 对应为文字 接口返回
     */
    fun setUserInfo(cities: List<City>, career: String, program: String): Disposable? {
        val token = setupWizardUseCase.user?.token ?: return null
        val cityCodesString = cities.map { it.id }.joinToString(separator = ",")
        return setupWizardUseCase.setUserInfo(token, cityCodesString, career, program)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "setUser==>$it")
                    if (it.code == 200) {
                        val user = setupWizardUseCase.user?.copy(cities = cities)
                        if (user != null) {
                            setupWizardUseCase.user = user
                        }
                        setupUserInfo.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }


}
