package com.snt.phoney.ui.dating.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.domain.model.Purpose
import com.snt.phoney.domain.usecase.CreateDatingUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateDatingViewModel @Inject constructor(private val usecase: CreateDatingUseCase) : ViewModel() {

    val programs = object : LiveData<List<DatingProgram>>() {
        override fun onActive() {
            usecase.user?.token?.let { token ->
                usecase.listDatingProgram(token, usecase.user!!.uuid ?: "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy {
                            value = it.data
                        }
            }
        }
    }


}
