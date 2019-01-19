package com.snt.phoney.ui.member

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.domain.model.OrderType
import com.snt.phoney.domain.model.MemberCombo
import com.snt.phoney.domain.usecase.GetMemberInfoUseCase
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.ui.model.PayViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MemberViewModel @Inject constructor(private val memberUsecase: GetMemberInfoUseCase, private val usecase: PayOrderUseCase) : PayViewModel(usecase) {

    val memberCombos = MutableLiveData<List<MemberCombo>>()

    fun listMemberCombo() {
        if (isLoading()) return
        val token = usecase.getAccessToken() ?: return
        memberUsecase.listMemberCombo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading(false)
                            if (it.success) {
                                memberCombos.value = it.data
                            }
                        },
                        onError = {
                            setLoading(false)
                        }
                ).disposedBy(disposeBag)
    }

    fun buyMemberWithWechat(target: String, uid: String? = null) {
        buyWithWechat(OrderType.BUY_MEMBER, target, uid)
    }

    fun buyMemberWithAlipay(target: String, uid: String? = null) {
        buyWithAlipay(OrderType.BUY_MEMBER, target, uid)
    }

}
