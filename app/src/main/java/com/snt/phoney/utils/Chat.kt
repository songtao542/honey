package com.snt.phoney.utils

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.text.TextUtils
import android.util.Log
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.eventbus.EventBus
import com.snt.phoney.domain.model.ImInfo
import com.snt.phoney.extensions.TAG
import com.snt.phoney.utils.data.Constants
import jiguang.chat.activity.ChatActivity
import jiguang.chat.model.Event
import jiguang.chat.model.EventType
import java.lang.Exception

object Chat {
    fun start(context: Context, im: ImInfo) {
        try {
            val intent = Intent()
            intent.setClass(context, ChatActivity::class.java)
            //创建会话
            intent.putExtra(jiguang.chat.utils.Constants.TARGET_ID, im.username)
            intent.putExtra(jiguang.chat.utils.Constants.TARGET_APP_KEY, Constants.JPush.APP_KEY)
            var name = im.nickname
            if (TextUtils.isEmpty(name)) {
                name = im.username
            }
            intent.putExtra(jiguang.chat.utils.Constants.CONV_TITLE, name)
            var conv: Conversation? = JMessageClient.getSingleConversation(im.username, Constants.JPush.APP_KEY)
            //如果会话为空，使用EventBus通知会话列表添加新会话
            if (conv == null) {
                conv = Conversation.createSingleConversation(im.username, Constants.JPush.APP_KEY)
                EventBus.getDefault().post(Event.Builder()
                        .setType(EventType.createConversation)
                        .setConversation(conv)
                        .build())
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }

    }
}