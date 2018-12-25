package com.snt.phoney.ui.album

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.usecase.UserInfoUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AlbumViewerViewModel @Inject constructor(private val usecase: UserInfoUseCase) : AppViewModel() {

    val toast = MutableLiveData<String>()
    val photos = MutableLiveData<List<Photo>>()

    fun deletePhotos(photoList: List<Photo>) {
        val token = usecase.getAccessToken() ?: return
        usecase.deletePhotos(token, photoList.map { it.id.toString() })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.success) {
                        //toast.value = context.getString(R.string.delete_photo_success)
                        Log.d("TTTT", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx${photoList[0]}")
                        photos.value = it.data
                    }
                }.disposedBy(disposeBag)
    }

}
