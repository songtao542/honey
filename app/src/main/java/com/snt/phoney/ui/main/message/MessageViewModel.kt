package com.snt.phoney.ui.main.message

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Applicant
import com.snt.phoney.domain.model.OfficialMessage
import com.snt.phoney.domain.usecase.GetDatingUseCase
import com.snt.phoney.domain.usecase.GetMessageUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MessageViewModel @Inject constructor(private val usecase: GetMessageUseCase, private val datingUsecase: GetDatingUseCase) : AppViewModel() {

    val messages = MutableLiveData<List<OfficialMessage>>()
    val applicants = MutableLiveData<List<Applicant>>()
    val error = MutableLiveData<String>()
    var pageIndex: Int = 0

    fun listOfficialMessage() {
        val token = usecase.getAccessToken() ?: return
        usecase.listOfficialMessage(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                messages.value = it.data
                            }
                        },
                        onError = {

                        }
                )
    }

    fun listApplicant() {
        val token = datingUsecase.getAccessToken() ?: return
        datingUsecase.listApplicant(token, pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                applicants.value = it.data
                            }
                        },
                        onError = {

                        }
                )
    }

}
