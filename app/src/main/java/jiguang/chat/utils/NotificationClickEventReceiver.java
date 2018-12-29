package jiguang.chat.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.snt.phoney.ui.main.MainActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.CommandNotificationEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import jiguang.chat.activity.ChatActivity;

public class NotificationClickEventReceiver {
    private Context mContext;

    public NotificationClickEventReceiver(Context context) {
        mContext = context.getApplicationContext();
    }

    @SuppressWarnings("unused")
    public void onEvent(CommandNotificationEvent event) {
        Log.d("TTTT", "mmmmmmmmmmmmmmmmmm eeee =" + event);
        Log.d("TTTT", "mmmmmmmmmmmmmmmmmm eeee getMsg==" + event.getMsg());
        Log.d("TTTT", "mmmmmmmmmmmmmmmmmm eeee getType=" + event.getType());
        event.getSenderUserInfo(new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                Log.d("TTTT", "mmmmmmmmmmmmmmmmmm eeee userInfo=" + userInfo);
            }
        });
        event.getTargetInfo(new CommandNotificationEvent.GetTargetInfoCallback() {
            @Override
            public void gotResult(int i, String s, Object o, CommandNotificationEvent.Type type) {
                Log.d("TTTT", "mmmmmmmmmmmmmmmmmm tttt iiii=" + i);
                Log.d("TTTT", "mmmmmmmmmmmmmmmmmm tttt ssss=" + s);
                Log.d("TTTT", "mmmmmmmmmmmmmmmmmm tttt oooo=" + o);
                Log.d("TTTT", "mmmmmmmmmmmmmmmmmm tttt type=" + type);
            }
        });
    }

    /**
     * 收到消息处理
     *
     * @param notificationClickEvent 通知点击事件
     */
    @SuppressWarnings("unused")
    public void onEvent(NotificationClickEvent notificationClickEvent) {
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
            notificationIntent.putExtra("fromNotification", true);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent main = new Intent(mContext, MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            mContext.startActivities(new Intent[]{main, notificationIntent});
        }
    }

}
