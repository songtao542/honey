package com.snt.phoney.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.GetFollowUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FollowMeViewModel @Inject constructor(private val usecase: GetFollowUseCase) : ViewModel() {

    val user: User? = usecase.user
    val follower = MutableLiveData<List<User>>()

    fun listFollow(): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.listFollow(token)
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

                        })
    }

}
