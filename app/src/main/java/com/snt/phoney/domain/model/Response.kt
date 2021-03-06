package com.snt.phoney.domain.model

import android.text.TextUtils
import com.snt.phoney.utils.life.SingleLiveData
import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(val data: T? = null, val code: Int = 0, val message: String? = null) {

    val hasMessage: Boolean
        get() {
            return !TextUtils.isEmpty(message)
        }

    val success: Boolean
        get() = code == 200

    /**
     * 针对返回List的请求才有作用
     */
    val isEmpty: Boolean
        get() {
            var result = true
            if (code == 200) {
                if (data != null && data is List<*>) {
                    result = data.isEmpty()
                }
            }
            return result
        }

    /**
     * 针对返回List的请求才有作用
     */
    val isNotEmpty: Boolean
        get() = !isEmpty

    companion object {

        fun <T> of(value: T): Response<T> {
            return Response(value, code = 200)
        }

    }
}


