package com.snt.phoney.ui.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Career
import com.snt.phoney.domain.model.City
import com.snt.phoney.domain.model.CityPickerConverter
import com.snt.phoney.domain.model.Purpose
import com.snt.phoney.domain.usecase.SetupWizardUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetupWizardViewModel @Inject constructor(private val setupWizardUseCase: SetupWizardUseCase) : AppViewModel() {

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
            setupWizardUseCase.getAccessToken()?.let { token ->
                setupWizardUseCase.listCareer(token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onSuccess = {
                                    value = it.data
                                },
                                onError = {}
                        ).disposedBy(disposeBag)
            }
        }
    }

    val purposes = object : LiveData<List<Purpose>>() {
        override fun onActive() {
            setupWizardUseCase.getAccessToken()?.let { token ->
                setupWizardUseCase.listPurpose(token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onSuccess = {
                                    value = it.data
                                },
                                onError = {}
                        ).disposedBy(disposeBag)
            }
        }
    }


    fun setSex(sex: Int) {
        val token = setupWizardUseCase.getAccessToken() ?: return
        setupWizardUseCase.setUserSex(token, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                setupSex.value = it.data
                                val user = setupWizardUseCase.getUser()
                                if (user != null) {
                                    user.sex = sex
                                    setupWizardUseCase.setUser(user)
                                }
                            } else if (it.hasMessage) {
                                error.value = it.message
                            }
                        },
                        onError = {}
                ).disposedBy(disposeBag)
    }


    fun setUserFeatures(height: Int, weight: Int, age: Int, cup: String) {
        val token = setupWizardUseCase.getAccessToken() ?: return
        setupWizardUseCase.setUserFeatures(token, height, weight, age, cup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                val user = setupWizardUseCase.getUser()?.copy(height = height, weight = weight.toDouble(), age = age, cup = cup)
                                if (user != null) {
                                    setupWizardUseCase.setUser(user)
                                }
                                setupFeatures.value = it.data
                            } else if (it.hasMessage) {
                                error.value = it.message
                            }
                        },
                        onError = {}
                ).disposedBy(disposeBag)
    }

    /**
     * @param cities,城市列表id用逗号分隔
     * @param career,职业 对于文字，接口返回
     * @param program,宣言 对应为文字 接口返回
     */
    fun setUserInfo(cities: List<City>, career: String, program: String) {
        val token = setupWizardUseCase.getAccessToken() ?: return
        val cityCodesString = cities.map { it.id }.joinToString(separator = ",")
        setupWizardUseCase.setUserInfo(token, cityCodesString, career, program)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                val user = setupWizardUseCase.getUser()?.copy(cities = cities, career = career, program = program)
                                if (user != null) {
                                    setupWizardUseCase.setUser(user)
                                }
                                setupUserInfo.value = it.data
                            } else if (it.hasMessage) {
                                error.value = it.message
                            }
                        },
                        onError = {}
                ).disposedBy(disposeBag)
    }


}
