package com.snt.phoney.ui.location

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.usecase.GetLocationUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocationViewModel @Inject constructor(private var usecase: GetLocationUseCase) : AppViewModel() {

    val location = MutableLiveData<Location>()

    private var locating = false

    fun getMyLocation(handler: ((Location) -> Unit)? = null) {
        if (locating) {
            return
        }
        locating = true
        usecase.getLocation()
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
                            error.value = context.getString(R.string.cannot_get_location)
                        }
                ).disposedBy(disposeBag)
    }


}