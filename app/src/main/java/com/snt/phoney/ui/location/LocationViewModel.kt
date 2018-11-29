package com.snt.phoney.ui.location

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.usecase.GetLocationUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocationViewModel @Inject constructor(private var usecase: GetLocationUseCase) : ViewModel() {

    val myLocation = MutableLiveData<Location>()

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
                            it?.let { location ->
                                if (handler == null) {
                                    myLocation.value = location
                                } else {
                                    handler.invoke(location)
                                }
                            }
                        },
                        onComplete = {
                            locating = false
                        })
    }


}