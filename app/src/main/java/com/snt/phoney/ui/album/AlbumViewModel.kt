package com.snt.phoney.ui.album

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Applicant
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.model.PhotoApply
import com.snt.phoney.domain.usecase.UserInfoUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.empty
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AlbumViewModel @Inject constructor(private val usecase: UserInfoUseCase) : AppViewModel() {

    val toast = MutableLiveData<String>()
    val photos = MutableLiveData<List<Photo>>()

    private val mPhotoApplyList by lazy { ArrayList<PhotoApply>() }
    val photoApplyList = MutableLiveData<List<PhotoApply>>()

    val reviewSuccess = MutableLiveData<String>()
    private var mPhotoApplyPageIndex = 1

    fun deletePhotos(photoList: List<Photo>) {
        val token = usecase.getAccessToken() ?: return
        usecase.deletePhotos(token, photoList.map { it.id.toString() })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.success) {
                        //toast.value = context.getString(R.string.delete_photo_success)
                        photos.value = it.data
                    }
                }.disposedBy(disposeBag)
    }


    fun listPhotoApply(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading()) {
            return
        }
        if (refresh) {
            mPhotoApplyPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listPhotoApply(token, mPhotoApplyPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading(false)
                            if (it.success) {
                                if (refresh) {
                                    photoApplyList.value = mPhotoApplyList.empty()
                                }
                                if (it.isNotEmpty) {
                                    mPhotoApplyPageIndex++
                                    photoApplyList.value = mPhotoApplyList.addList(it.data)
                                } else {
                                    loadMore?.isEnable = false
                                }
                            }
                        },
                        onError = {
                            setLoading(false)
                            loadMore?.isLoadFailed = true
                        }
                ).disposedBy(disposeBag)
    }

    fun reviewPhotoApply(photoApply: PhotoApply, state: Int) {
        val token = usecase.getAccessToken() ?: return
        usecase.reviewPhotoApply(token, photoApply.safeUuid, state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                when (state) {
                                    1 -> {
                                        photoApply.state = 1
                                        reviewSuccess.value = context.getString(R.string.has_agree)
                                    }
                                    2 -> {
                                        photoApply.state = 2
                                        reviewSuccess.value = context.getString(R.string.has_reject)
                                    }
                                }
                            } else {
                                error.value = context.getString(R.string.review_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.review_failed)
                        }
                ).disposedBy(disposeBag)
    }

}
