package com.snt.phoney.domain.repository.impl

import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.snt.phoney.domain.repository.JMessageRepository
import javax.inject.Inject

class JMessageRepositoryImpl @Inject constructor() : JMessageRepository {

    override fun login(username: String, password: String, callback: ((responseCode: Int, responseMessage: String) -> Unit)?) {
        JMessageClient.login(username, password,
                object : BasicCallback() {
                    override fun gotResult(responseCode: Int, responseMessage: String) {
                        callback?.invoke(responseCode, responseMessage)
                    }
                })
    }
}