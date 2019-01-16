package com.snt.phoney.ui.user

import android.util.Log
import com.snt.phoney.R
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.domain.usecase.UserInfoUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.ui.model.PayViewModel
import com.snt.phoney.utils.life.SingleLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserInfoViewModel @Inject constructor(private val usecase: UserInfoUseCase,
                                            private val payOrderUseCase: PayOrderUseCase) : PayViewModel(payOrderUseCase) {

    val userInfo = SingleLiveData<User>()

    val followSuccess = SingleLiveData<Boolean>()

    fun getUser(): User? {
        return usecase.getUser()
    }

    fun getUserInfo(uuid: String) {
        val token = usecase.getAccessToken() ?: return
        val viewerId = usecase.getUser()?.uuid ?: ""
        usecase.getLocation()
                .flatMap {
                    usecase.getUserInfo(token, uuid, it.latitude, it.longitude)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    it.data?.let { data ->
                        data.fixPhotos()
                        val burnedPhotos = usecase.getBurnedPhotoFromLocal(viewerId)
                        data.photos?.forEach { p ->
                            burnedPhotos.forEach { pb ->
                                if (p.id == pb.id) {
                                    p.burn = pb.burn
                                }
                            }
                        }
                        return@let
                    }
                    return@map it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                userInfo.value = it.data
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        },
                        onComplete = {}
                ).disposedBy(disposeBag)
    }

    fun getUserWechatAccount(uuid: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.getUserWechatAccount(token, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                val user = userInfo.value
                                user?.wechatAccount = it.data
                                userInfo.value = user
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.get_wechat_account_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.get_wechat_account_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun applyToViewPhotos(target: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.applyToViewPhotos(token, target)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                success.value = context.getString(R.string.has_applyed_to_view_photo)
                            } else if (it.isNotEmpty) {
                                error.value = it.data
                            } else {
                                error.value = context.getString(R.string.has_applyed_to_view_photo_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.has_applyed_to_view_photo_failed)
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
                            @Suppress("CascadeIf")
                            if (it.success) {
                                followSuccess.value = it.data
                            } else if (it.hasMessage) {
                                error.value = it.message
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
