package com.snt.phoney.ui.dating.detail

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.domain.usecase.DatingUseCase
import com.snt.phoney.domain.usecase.UserInfoUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DatingDetailViewModel @Inject constructor(private val usecase: DatingUseCase, private val userUsecase: UserInfoUseCase) : AppViewModel() {

    val dating = MutableLiveData<Dating>()
    val joinSuccess = MutableLiveData<String>()
    val followSuccess = MutableLiveData<Boolean>()

    fun getCurrentUserId(): String {
        return usecase.getUser()?.safeUuid ?: ""
    }

    fun getDatingDetail(uuid: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.getLocation()
                .flatMap {
                    usecase.getDatingDetail(token, uuid, it.latitude, it.longitude)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (it.code == 200) {
                                dating.value = it.data
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        },
                        onComplete = {}
                )
                .disposedBy(disposeBag)
    }

    fun joinDating(uuid: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.joinDating(token, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                joinSuccess.value = it.data
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.join_dating_failed)
                        }
                )
                .disposedBy(disposeBag)
    }

    fun follow(userId: String) {
        val token = userUsecase.getAccessToken() ?: return
        userUsecase.follow(token, userId)
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
