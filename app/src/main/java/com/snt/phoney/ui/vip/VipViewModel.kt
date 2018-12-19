package com.snt.phoney.ui.vip

import android.app.Activity
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.domain.model.OrderType
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.VipCombo
import com.snt.phoney.domain.usecase.GetVipInfoUseCase
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.utils.AlipayApi
import com.snt.phoney.wxapi.WXPayViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import javax.inject.Inject

class VipViewModel @Inject constructor(private val vipUsecase: GetVipInfoUseCase, private val usecase: PayOrderUseCase) : WXPayViewModel(usecase) {

    val vipCombos = MutableLiveData<List<VipCombo>>()

    var activityRef: WeakReference<Activity>? = null

//    private val wechatApi: WechatApi by lazy { WechatApi(application) }
//
//    val vipInfo = MutableLiveData<VipInfo>()
//    val mibiAmount = MutableLiveData<Int>()
//    val mibiWallet = MutableLiveData<MibiWallet>()
//    fun getMibiAmount(): Disposable? {
//        val token = usecase.user?.token ?: return null
//        return vipUsecase.getMibiAmount(token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeBy {
//                    if (it.code == Response.SUCCESS) {
//                        mibiAmount.value = it.data
//                    }
//                }
//    }
//
//
//    fun getMibiWallet(): Disposable? {
//        val token = usecase.user?.token ?: return null
//        return vipUsecase.getMibiWallet(token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeBy {
//                    if (it.code == Response.SUCCESS) {
//                        mibiWallet.value = it.data
//                    }
//                }
//    }
//
//
//    fun getVipInfo(): Disposable? {
//        val token = usecase.user?.token ?: return null
//        return vipUsecase.getVipInfo(token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeBy {
//                    if (it.code == Response.SUCCESS) {
//                        vipInfo.value = it.data
//                    }
//                }
//    }

    fun setActivity(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    fun listVipCombo(): Disposable? {
        val token = usecase.user?.token ?: return null
        return vipUsecase.listVipCombo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == Response.SUCCESS) {
                        vipCombos.value = it.data
                    }
                }
    }

    fun buyVipWithWechat(type: OrderType, target: String, uid: String? = null): Disposable? {
        return buy(type, target, uid)
    }

    fun buyVipWithAlipay(type: OrderType, target: String, uid: String? = null): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.createOrder(token, type.value.toString(), target, uid ?: "")
                .flatMap {
                    return@flatMap if (it.code == Response.SUCCESS && !TextUtils.isEmpty(it.data)) {
                        usecase.alipay(token, it.data!!)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                    } else {
                        Single.create<Response<String>> { Response<String>(code = 999, message = context.getString(R.string.create_order_failed)) }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == Response.SUCCESS && it.data != null) {
                                payByAli(it.data)
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.buy_vip_failed)
                        }
                )
    }

    private fun payByAli(orderInfo: String) {
        activityRef?.get()?.let { activity ->
            AlipayApi.pay(activity, orderInfo) {
                if (it == 9000) {
                    success.value = context.getString(R.string.buy_vip_success)
                } else {
                    error.value = context.getString(R.string.buy_vip_failed)
                }
            }
        }
    }

}
