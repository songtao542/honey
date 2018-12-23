package com.snt.phoney.ui.main.mine

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.AmountInfo
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.domain.usecase.GetUserInfoUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.consumesAll
import kotlinx.serialization.getSerialTag
import java.io.File
import javax.inject.Inject

class MineViewModel @Inject constructor(private val usecase: GetUserInfoUseCase) : AppViewModel() {

    val user = usecase.getUser()

    val success = MutableLiveData<String>()
    val toast = MutableLiveData<String>()
    val error = MutableLiveData<String>()
    val photos = MutableLiveData<List<Photo>>()

    val amountInfo = MutableLiveData<AmountInfo>()

    fun getUserAmountInfo(): Disposable? {
        val token = usecase.getAccessToken() ?: return null
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

    fun setPhotoPermission(photoPermission: PhotoPermission, money: Double = 0.0, photoId: String = ""): Disposable? {
        val token = usecase.getAccessToken() ?: return null
        return usecase.setPhotoPermission(token, photoPermission.value, money, photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                success.value = context.getString(R.string.set_photo_permission_success)
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = context.getString(R.string.set_photo_permission_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.set_photo_permission_failed)
                        }
                )
    }

    fun uploadPhotos(photoPaths: List<File>) {
        val token = usecase.getAccessToken() ?: return
        usecase.uploadPhotos(token, photoPaths)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                photos.value = it.data
                                success.value = context.getString(R.string.upload_photo_success)
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = context.getString(R.string.upload_photo_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.upload_photo_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun getUserPhotos() {
        val token = usecase.getAccessToken() ?: return
        usecase.getUserPhotos(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == 200) {
                        photos.value = it.data
                    }
                }.disposedBy(disposeBag)
    }

    fun deletePhotos(photoList: List<Photo>) {
        val token = usecase.getAccessToken() ?: return
        usecase.deletePhotos(token, photoList.map { it.id.toString() })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == 200) {
                        //toast.value = context.getString(R.string.delete_photo_success)
                        Log.d("TTTT", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx${photoList[0]}")
                        photos.value = it.data
                    }
                }.disposedBy(disposeBag)
    }

    fun signOut() {
        /***********test**************/
        usecase.getAccessToken()?.let { usecase.deleteUser(it) }
        /***********test**************/
        usecase.setUser(null)
    }
}
