package com.snt.phoney.ui.main.mine

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.model.UserInfo
import com.snt.phoney.domain.usecase.UserInfoUseCase
import com.snt.phoney.extensions.TAG
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.utils.WechatApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class MineViewModel @Inject constructor(private val usecase: UserInfoUseCase) : AppViewModel() {

    val user = object : MutableLiveData<User>() {
        override fun onActive() {
            postValue(usecase.getUser())
        }
    }

    val toast = MutableLiveData<String>()
    val photos = MutableLiveData<List<Photo>>()

    val userInfo = MutableLiveData<UserInfo>()

    fun getAllInfoOfUser() {
        val token = usecase.getAccessToken() ?: return
        usecase.getAllInfoOfUser(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                userInfo.value = it.data
                            }
                        },
                        onError = {}
                ).disposedBy(disposeBag)
    }

    fun setWalletNewsToRead() {
        val token = usecase.getAccessToken() ?: return
        usecase.setWalletNewsToRead(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            Log.d(TAG, "setWalletNewsToRead $it")
                        },
                        onError = {}
                ).disposedBy(disposeBag)
    }

    fun setPhotoPermission(photoPermission: PhotoPermission, money: Double = 0.0, photoId: String = "") {
        val token = usecase.getAccessToken() ?: return
        usecase.setPhotoPermission(token, photoPermission.value, money, photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                success.value = context.getString(R.string.set_photo_permission_success)
                                updateUserPhotoPermission(photoPermission)
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = context.getString(R.string.set_photo_permission_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.set_photo_permission_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun uploadPhotos(photoPaths: List<File>) {
        val token = usecase.getAccessToken() ?: return
        usecase.uploadPhotos(token, photoPaths)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
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
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                photos.value = it.data
                            }
                        },
                        onError = {}
                ).disposedBy(disposeBag)
    }

    fun uploadHeadIcon(file: File) {
        val token = usecase.getAccessToken() ?: return
        usecase.uploadHeadIcon(token, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                if (!TextUtils.isEmpty(it.data)) {
                                    val updatedUser = user.value
                                    updatedUser?.avatar = it.data
                                    updatedUser?.let { updated ->
                                        user.value = updated
                                        usecase.setUser(updated)
                                    }
                                }
                                success.value = context.getString(R.string.upload_photo_success)
                            } else {
                                error.value = context.getString(R.string.upload_photo_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.upload_photo_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun updateUserPhotoPermission(permission: PhotoPermission) {
        user.value?.let { user ->
            user.photoPermission = permission.value
            usecase.setUser(user)
        }
    }

    fun signOut() {
        /***********test**************/
        //usecase.getAccessToken()?.let { usecase.deleteUser(it) }
        /***********test**************/
        usecase.setUser(null)
    }
}
