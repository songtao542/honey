package com.snt.phoney.ui.user

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.GetUserInfoUseCase
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.ui.model.PayViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserInfoViewModel @Inject constructor(private val usecase: GetUserInfoUseCase,
                                            private val payOrderUseCase: PayOrderUseCase) : PayViewModel(payOrderUseCase) {

    val userInfo = MutableLiveData<User>()

    val followSuccess = MutableLiveData<Boolean>()

    fun getUserInfo(uuid: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.getLocation()
                .flatMap {
                    usecase.getUserInfo(token, uuid, it.latitude, it.longitude)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (it.code == 200) {
                                userInfo.value = it.data
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }


    fun follow(uuid: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.follow(token, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                followSuccess.value = it.data
                            } else {
                                error.value = context.getString(R.string.follow_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.follow_failed)
                        }
                ).disposedBy(disposeBag)
    }

}
