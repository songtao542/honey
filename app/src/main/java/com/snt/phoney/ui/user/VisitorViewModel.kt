package com.snt.phoney.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.model.UserInfo
import com.snt.phoney.domain.usecase.GetVisitorUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VisitorViewModel @Inject constructor(private val usecase: GetVisitorUseCase) : ViewModel() {

    val user: User? = usecase.user

    val visitors = MutableLiveData<List<UserInfo>>()

    fun listVisitor(): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.listVisitor(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            Log.d("TTTT", "list follow==========>$it")
                            if (it.code == 200) {
                                visitors.value = it.data
                            }
                        },
                        onError = {

                        })
    }

}
