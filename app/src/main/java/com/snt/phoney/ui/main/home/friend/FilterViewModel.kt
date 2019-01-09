package com.snt.phoney.ui.main.home.friend

import androidx.lifecycle.LiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.DatingUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val usecase: DatingUseCase) : AppViewModel() {

    val programs = object : LiveData<List<DatingProgram>>() {
        override fun onActive() {
            usecase.getAccessToken()?.let { token ->
                usecase.listDatingProgram(token, usecase.getUser()!!.uuid ?: "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy {
                            value = it.data
                        }
            }
        }
    }

    fun getUser(): User? {
        return usecase.getUser()
    }

}
