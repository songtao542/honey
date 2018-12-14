package com.snt.phoney.ui.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.domain.usecase.AlbumSettingUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PaySettingViewModel @Inject constructor(private val usecase: AlbumSettingUseCase) : ViewModel() {

    val photos = MutableLiveData<List<Photo>>()

    fun setPhotoPermission(photoPermission: PhotoPermission, money: Double = 0.0, photoId: String = ""): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.setPhotoPermission(token, photoPermission.value, money, photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {

                }
    }

    fun getUserPhotoes(): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.getUserPhotoes(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {

                }
    }
}
