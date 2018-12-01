package com.snt.phoney.ui.location

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amap.api.location.AMapLocation
import com.snt.phoney.domain.usecase.GetLocationUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocationViewModel @Inject constructor(private var usecase: GetLocationUseCase) : ViewModel() {

    val location = MutableLiveData<Location>()
    val error = MutableLiveData<String>()

    private var locating = false

    fun getMyLocation(handler: ((Location) -> Unit)? = null): Disposable? {
        if (locating) {
            return null
        }
        locating = true
        return usecase.getLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            locating = false
                            it?.let { loc ->
                                location.value = loc
                                handler?.invoke(loc)
                                return@let
                            }
                        },
                        onError = {
                            locating = false
                            error.value = "无法获取位置"
                        })
    }



}