package com.snt.phoney.domain.repository.impl

import android.app.Application
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.snt.phoney.domain.repository.JMessageRepository
import com.snt.phoney.service.VoiceCallEngine
import javax.inject.Inject

class JMessageRepositoryImpl @Inject constructor(private val application: Application) : JMessageRepository {

    override fun login(username: String, password: String, callback: ((responseCode: Int, responseMessage: String) -> Unit)?) {
        JMessageClient.login(username, password,
                object : BasicCallback() {
                    override fun gotResult(responseCode: Int, responseMessage: String) {
                        VoiceCallEngine.init(application)
                        callback?.invoke(responseCode, responseMessage)
                    }
                })
    }

    override fun logout() {
        VoiceCallEngine.release()
        JMessageClient.logout()
    }
}