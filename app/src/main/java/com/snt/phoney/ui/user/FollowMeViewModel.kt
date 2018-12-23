package com.snt.phoney.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.GetFollowUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FollowMeViewModel @Inject constructor(private val usecase: GetFollowUseCase) : AppViewModel() {

    val user: User? = usecase.getUser()
    val follower = MutableLiveData<List<User>>()

    fun listFollow() {
        val token = usecase.getAccessToken() ?: return
        usecase.listFollow(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            Log.d("TTTT", "list follow==========>$it")
                            if (it.code == 200) {
                                follower.value = it.data
                            }
                        },
                        onError = {

                        }
                ).disposedBy(disposeBag)
    }

}
