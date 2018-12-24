package com.snt.phoney.ui.report

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.ReportReason
import com.snt.phoney.domain.usecase.ReportUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject


class ReportViewModel @Inject constructor(private val usecase: ReportUseCase) : AppViewModel() {

    val reportReasons = MutableLiveData<List<ReportReason>>()
    val reportSuccess = MutableLiveData<String>()

    fun listReportReasons() {
        usecase.listReportReasons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                reportReasons.value = it.data
                            } else {
                                error.value = context.getString(R.string.load_report_reason_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_report_reason_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun report(reasonType: String,
               targetUid: String,
               content: String,
               type: Int,
               file: File) {
        val token = usecase.getAccessToken() ?: return
        usecase.report(token, reasonType, targetUid, content, type.toString(), file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                reportSuccess.value = context.getString(R.string.report_success)
                            } else {
                                error.value = context.getString(R.string.report_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.report_failed)
                        }
                ).disposedBy(disposeBag)
    }

}
