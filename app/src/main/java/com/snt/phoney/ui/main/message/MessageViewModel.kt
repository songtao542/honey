package com.snt.phoney.ui.main.message

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Applicant
import com.snt.phoney.domain.model.OfficialMessage
import com.snt.phoney.domain.model.PhotoApply
import com.snt.phoney.domain.usecase.DatingUseCase
import com.snt.phoney.domain.usecase.GetMessageUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MessageViewModel @Inject constructor(private val usecase: GetMessageUseCase, private val datingUsecase: DatingUseCase) : AppViewModel() {

    val messages = MutableLiveData<List<OfficialMessage>>()
    val applicants = MutableLiveData<List<Applicant>>()
    val photoApplyList = MutableLiveData<List<PhotoApply>>()

    private var mApplicantPageIndex: Int = 1
    private var mPhotoApplyPageIndex: Int = 1

    fun listOfficialMessage() {
        val token = usecase.getAccessToken() ?: return
        usecase.listOfficialMessage(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                messages.value = it.data
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun listApplicant() {
        val token = datingUsecase.getAccessToken() ?: return
        datingUsecase.listApplicant(token, mApplicantPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                applicants.value = it.data
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun listPhotoApply() {
        val token = datingUsecase.getAccessToken() ?: return
        usecase.listPhotoApply(token, mPhotoApplyPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                photoApplyList.value = it.data
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }

}
