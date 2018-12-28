package jiguang.chat.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import jiguang.chat.activity.ChatActivity;

public class NotificationClickEventReceiver {
    private Context mContext;

    public NotificationClickEventReceiver(Context context) {
        mContext = context;
    }

    /**
     * 收到消息处理
     *
     * @param notificationClickEvent 通知点击事件
     */
    @SuppressWarnings("unused")
    public void onEvent(NotificationClickEvent notificationClickEvent) {
        Log.d("TTTT", "xxxxxxxxxxxxxx new message coming");
        if (null == notificationClickEvent) {
            return;
        }
        Message msg = notificationClickEvent.getMessage();
        if (msg != null) {
            Object targetInfo = msg.getTargetInfo();
            //String targetId = msg.getTargetInfo();
            String appKey = msg.getFromAppKey();
            ConversationType type = msg.getTargetType();
            Conversation conv;
            Intent notificationIntent = new Intent(mContext, ChatActivity.class);
            if (type == ConversationType.single) {
                String targetId = ((UserInfo) targetInfo).getUserName();
                conv = JMessageClient.getSingleConversation(targetId, appKey);
                notificationIntent.putExtra(Constants.TARGET_ID, targetId);
                notificationIntent.putExtra(Constants.TARGET_APP_KEY, appKey);
            } else {
                long targetId = ((GroupInfo) targetInfo).getGroupID();
                conv = JMessageClient.getGroupConversation(targetId);
                notificationIntent.putExtra(Constants.GROUP_ID, targetId);
            }
            notificationIntent.putExtra(Constants.CONV_TITLE, conv.getTitle());
            conv.resetUnreadCount();
            //notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.putExtra("fromGroup", false);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(notificationIntent);
        }
    }

}
