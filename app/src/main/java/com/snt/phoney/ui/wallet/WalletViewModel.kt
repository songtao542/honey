package com.snt.phoney.ui.wallet

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.domain.model.MibiWallet
import com.snt.phoney.domain.model.OrderType
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.VipInfo
import com.snt.phoney.domain.usecase.GetWalletUseCase
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.ui.model.PayViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WalletViewModel @Inject constructor(private val usecase: GetWalletUseCase, private val payUsecase: PayOrderUseCase) : PayViewModel(payUsecase) {

    val vipInfo = MutableLiveData<VipInfo>()
    val mibiAmount = MutableLiveData<Int>()
    val mibiWallet = MutableLiveData<MibiWallet>()

    fun getMibiAmount() {
        val token = usecase.getAccessToken() ?: return
        usecase.getMibiAmount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == Response.SUCCESS) {
                        mibiAmount.value = it.data
                    }
                }.disposedBy(disposeBag)
    }


    fun getMibiWallet() {
        val token = usecase.getAccessToken() ?: return
        usecase.getMibiWallet(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == Response.SUCCESS) {
                        mibiWallet.value = it.data
                    }
                }.disposedBy(disposeBag)
    }


    fun getVipInfo() {
        val token = usecase.getAccessToken() ?: return
        usecase.getVipInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == Response.SUCCESS) {
                        vipInfo.value = it.data
                    }
                }.disposedBy(disposeBag)
    }


    fun buyMibiWithWechat(target: String, uid: String? = null) {
        buyWithWechat(OrderType.BUY_MIBI, target, uid)
    }

    fun buyMibiWithAlipay(target: String, uid: String? = null) {
        buyWithAlipay(OrderType.BUY_MIBI, target, uid)
    }


}
