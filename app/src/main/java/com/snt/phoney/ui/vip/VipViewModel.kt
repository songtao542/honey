package com.snt.phoney.ui.vip

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.domain.model.OrderType
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.VipCombo
import com.snt.phoney.domain.usecase.GetVipInfoUseCase
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.ui.model.PayViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VipViewModel @Inject constructor(private val vipUsecase: GetVipInfoUseCase, private val usecase: PayOrderUseCase) : PayViewModel(usecase) {

    val vipCombos = MutableLiveData<List<VipCombo>>()

    fun listVipCombo() {
        val token = usecase.getAccessToken() ?: return
        vipUsecase.listVipCombo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == Response.SUCCESS) {
                        vipCombos.value = it.data
                    }
                }.disposedBy(disposeBag)
    }

    fun buyVipWithWechat(target: String, uid: String? = null) {
        buyWithWechat(OrderType.BUY_VIP, target, uid)
    }

    fun buyVipWithAlipay(target: String, uid: String? = null) {
        buyWithAlipay(OrderType.BUY_VIP, target, uid)
    }

}
