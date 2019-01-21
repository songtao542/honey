package com.snt.phoney.ui.main

import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.amap.api.location.AMapLocation
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.usecase.JMessageUseCase
import com.snt.phoney.domain.usecase.UpdateUserLocationUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val usecase: UpdateUserLocationUseCase, private val jMessageUseCase: JMessageUseCase) : AppViewModel() {

    private fun updateUserLocation() {
        val token = usecase.getAccessToken() ?: return
        usecase.getLocation()
                .flatMap {
                    val aMapLocation = it as AMapLocation
                    return@flatMap usecase.updateUserLocation(token, it.latitude, it.longitude, aMapLocation.address, aMapLocation.city)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                        },
                        onError = {
                        },
                        onComplete = {
                        }
                ).disposedBy(disposeBag)

    }

    private fun loginJMessage() {
        val im = jMessageUseCase.getUser()?.im
        im?.let {
            val username = it.username
            val password = it.password
            if (username != null && password != null) {
                jMessageUseCase.login(username, password) { code, res ->
                    Log.d("JMessage", "极光IM登录结果: $code ,  $res")
                }
            }
        }
    }

    private fun updateJPushAlias() {
        val deviceToken = usecase.getUser()?.deviceToken
        deviceToken?.let { JPushInterface.setAlias(application, 1, it) }
    }

    fun init() {
        updateUserLocation()
        updateJPushAlias()
        loginJMessage()
    }

}