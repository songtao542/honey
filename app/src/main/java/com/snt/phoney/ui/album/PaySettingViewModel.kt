package com.snt.phoney.ui.album

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.domain.usecase.AlbumSettingUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PaySettingViewModel @Inject constructor(private val usecase: AlbumSettingUseCase) : AppViewModel() {

    val photos = MutableLiveData<List<Photo>>()

    fun setPhotoPermission(photoPermission: PhotoPermission, money: Double = 0.0, photoId: String = ""): Disposable? {
        val token = usecase.getAccessToken() ?: return null
        return usecase.setPhotoPermission(token, photoPermission.value, money, photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                success.value = context.getString(R.string.set_photo_permission_success)
                            } else {
                                error.value = context.getString(R.string.set_photo_permission_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.set_photo_permission_failed)
                        })

    }

    fun getUserPhotos(): Disposable? {
        val token = usecase.getAccessToken() ?: return null
        return usecase.getUserPhotos(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == 200) {
                        photos.value = it.data
                    }
                }
    }
}
