package com.snt.phoney.utils

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.alipay.sdk.app.PayTask
import kotlinx.coroutines.*


object AlipayApi {
    //val appid = "2016092400585139"
    //val privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCjPHjDh2x5SJzQHZEX7hf0372eXo2YNqXDOAfxsxhD/12mluccXFuxJcVRepaQ++toclaceJTohHAW7DMwVJuB34Bd4+ahq2+esP+nM0K60bIUfgxZrnqYONwdY8oSsLybvVGWM2qeNh/YA164iYdiOX++EyLlWBMDzoQpetOHe/NiGZ5kWrq+U6pIZR5cnpxp4gRdzyZ8uybjNl49Y8xrx17D4uvmHrHRmmmgg5Sa++jcxPh5X11ztEB5HxPrvhZA+HMq59qajvltXZQb8PY8pt2/dhEumNI00NN2f2DV/wDLzJ1qhe9NOR05cxPYjlIfim0XzkIOHvX3MfoHGrXtAgMBAAECggEAVDy0pVKRa9VI9y0K3yqDOl7r33oz38TzrdaQYu5lK1Hw5jMheMVAnB49wVpdsNwQE4pNn9mqHoOZxHqHv/0pcs//0nP1ZKCvrVjx1TriyL3GBEU34l/j7AILtS717Mc/za7ZF4XZE2Qi8rpqexidwNajUVdWvrvAtEoycaVs25EKASlniUXEN8LdGv8iIBCCRNL6nM6Diguf88Bl5BIRTgDD/0P29bKtUxaiS7a+zUAqZWdoDD+b6fpqxsqry2Y769NxLk0bHVwcbQ7NJmpGN2CjFbF+FgUV/ILx0dC/Wl1vWv8gVusGbKPvlm71kdFL/tSTDLtn15GkwFfx3qnhgQKBgQDuX2Q8U3qdcMIWBc+V5IjFfZKhPOG+MPK9EFkAfuPrdjNyRdzUJ3EXyc3MDu+lNBNg0pLjQ30+C5Phn5sYyIBrazJLHk96+4ABZWBZcK2mUsOp9XXtUDpLvAd/Lz492WcqkNwgRyP/9ruyQHYObWWKaPxcRshpBwtHEmTc8x8UaQKBgQCvTq4r8GCaTx6q9FgXNJNqL1YFuQkErWfARZ2O4baM4H0b/4XZTJQVs9a/hdJ4aOJbfTU4hGWHGMAUuFmpw+Fa4rziPs8a9mqLzxpPP7CUmg2iInAjz8/TRl3a6yjnzM79f1lJKnEvUVMl68SVQ2H2INOCUZB8cgmg4+r7LWNU5QKBgBn+xySZCfcfc9sI+TFkgdHfttzkkc228tHRdZNLQznLYyLkIYf1YSzx8gJ94n6GplJZxHEKTB83DgJ98GEMhIyyXQK6JHrJ8JS2ZIJ8ekN4JSRkw16E8EaQE9U35rbB/bEzqP3QJEnms2NmCjnajkdA2byKb5KclQtEI0HBNbN5AoGAIwn74AZkmJMV0x+Rd9nnWQLrE6u/NLMgGJsRedx9azz3ZKA29Y7Lzmaqlo892RlFnPmyqIA19sGoqruKGFdxdIP7MtFQ/degxh736D0XoywDa5OsjEveAyF+YuI7w9D40kwg4XHdO5LqppqwKRFVxCcdf1+kHGuCpyjG77tLrk0CgYEAlXXC9pOqpQq8FU544wdebRnfaQujX9+1zU28ktTzzjhGG9SHyAQebgjlUqiGGoKX2ijbRLsyGvxiaGH+ejiFNBSuToV3yKbazCF1y2C6o0BAo5dRvzwPKpABlALhyf/SfgWtYfY69BOW60jlL79i1l7FVW2mzXOtDHH93f/xBtc="

    @JvmStatic
    fun pay(context: Activity, orderInfo: String, callback: ((status: Int) -> Unit)) {
        var status = 0
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                delay(2000)
                Log.d("TTTT", "thread=======111========${Thread.currentThread().name}")
                Log.d("TTTT", "orderInfo=======111========${orderInfo}")
                val alipay = PayTask(context)
                val result = alipay.payV2(orderInfo, true)

                val payResult = PayResult(result)
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                val resultInfo = payResult.result// 同步返回需要验证的信息
                val resultStatus = payResult.resultStatus
                Log.d("TTTT", "resultInfo===============${resultInfo}")
                Log.d("TTTT", "resultStatus===============${resultStatus}")
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    status = 9000
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                }
            }
            Log.d("TTTT", "thread=======222========${Thread.currentThread().name}")
            Log.d("TTTT", "thread=======33333========${Thread.currentThread().name}")
            callback.invoke(status)
        }
    }

}

class PayResult {
    var resultStatus: String? = null
    var result: String? = null
    var memo: String? = null

    constructor(rawResult: Map<String, String>) {
        if (rawResult == null) {
            return
        }

        for (key in rawResult.keys) {
            when {
                TextUtils.equals(key, "resultStatus") -> resultStatus = rawResult[key]
                TextUtils.equals(key, "result") -> result = rawResult[key]
                TextUtils.equals(key, "memo") -> memo = rawResult[key]
            }
        }
    }


    override fun toString(): String {
        return "resultStatus={$resultStatus};memo={$memo};result={$result}"
    }

}