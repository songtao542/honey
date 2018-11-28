package com.snt.phoney.ui.main.mine

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.model.AmountInfo
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.domain.usecase.GetUserInfoUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MineViewModel @Inject constructor(private val usecase: GetUserInfoUseCase) : ViewModel() {

    val user = usecase.user

    val amountInfo = MutableLiveData<AmountInfo>()

    fun getUserAmountInfo(): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.getUserAmountInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "rrrrrrrrrrrrrrrr$it")
                    if (it.code == 200) {
                        amountInfo.value = it.data
                    }
                }
    }

    fun setPhotoPermission(photoPermission: PhotoPermission, money: Double, photoId: String): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.setPhotoPermission(token, photoPermission.value, money, photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {

                }
    }

    fun signOut() {
        /***********test**************/
        val token = usecase.user?.token ?: ""
        usecase.deleteUser(token)
        /***********test**************/
        usecase.user = null
    }
}
