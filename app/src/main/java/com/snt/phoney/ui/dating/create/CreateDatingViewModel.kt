package com.snt.phoney.ui.dating.create

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.domain.model.PoiAddress
import com.snt.phoney.domain.usecase.DatingUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class CreateDatingViewModel @Inject constructor(private val usecase: DatingUseCase) : AppViewModel() {

    val programs = object : LiveData<List<DatingProgram>>() {
        override fun onActive() {
            usecase.getAccessToken()?.let { token ->
                usecase.listDatingProgram(token, usecase.getUser()!!.uuid ?: "")
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

    fun publish(title: String, program: String, content: String, days: Int, location: PoiAddress, cover: List<File>) {
        val token = usecase.getAccessToken() ?: return
        val address = location.address ?: location.formatAddress ?: ""
        val city = location.city ?: ""
        usecase.publishDating(token, title, program, content, days, city, address, location.latitude, location.longitude, cover)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                success.value = it.data
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.publish_failed)
                        }
                ).disposedBy(disposeBag)

    }


}
