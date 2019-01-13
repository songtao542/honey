package com.sanyou.broadcast.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import cn.jpush.android.api.JPushInterface
import com.snt.phoney.R
import com.snt.phoney.ui.main.MainActivity
import jiguang.chat.activity.ChatActivity
import org.json.JSONException
import org.json.JSONObject

const val TAG = "PushMessageReceiver"

class PushMessageReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val bundle = intent?.extras ?: Bundle()
            val action = intent?.action ?: ""
            Log.d(TAG, "[PushMessageReceiver] onReceive - " + action + ", extras: " + printBundle(bundle!!))

            if (JPushInterface.ACTION_REGISTRATION_ID == action) {
                val regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                Log.d(TAG, "接收Registration Id : $regId")
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED == action) {
                Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE))
                processCustomMessage(context!!, bundle)
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED == action) {
                Log.d(TAG, "接收到推送下来的通知")
                val notificationId = bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                Log.d(TAG, "接收到推送下来的通知的ID: $notificationId")

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED == action) {
                Log.d(TAG, "用户点击打开了通知")
                //打开自定义的Activity
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtras(bundle)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context?.startActivity(intent)
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK == action) {
                Log.d(TAG, "用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA))
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE == action) {
                val connected = intent?.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                        ?: false
                Log.w(TAG, "action connected state change to $connected")
            } else {
                Log.d(TAG, "Unhandled intent - $action")
            }
        } catch (e: Exception) {

        }

    }


    // 打印所有的 intent extra 数据
    private fun printBundle(bundle: Bundle): String {
        val sb = StringBuilder()
        for (key in bundle.keySet()) {
            if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key))
            } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
            } else if (key == JPushInterface.EXTRA_EXTRA) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data")
                    continue
                }

                try {
                    val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                    val it = json.keys()

                    while (it.hasNext()) {
                        val myKey = it.next()
                        sb.append(
                                "\nkey:" + key + ", value: [" +
                                        myKey + " - " + json.optString(myKey) + "]"
                        )
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Get message extra JSON error!")
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key))
            }
        }
        return sb.toString()
    }

    private fun processCustomMessage(context: Context, bundle: Bundle) {
        val message = bundle.getString(JPushInterface.EXTRA_MESSAGE)
        //val extras = bundle.getString(JPushInterface.EXTRA_EXTRA)
        val title = bundle.getString(JPushInterface.EXTRA_TITLE)
        if (!TextUtils.isEmpty(message)) {
            val builder = NotificationCompat.Builder(context, "notice")
            builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                    .setContentTitle(title ?: context.getString(R.string.app_name))
                    .setContentText(message)
                    .setShowWhen(true)
                    .setTicker(message)
                    .setAutoCancel(true)


            val intent = Intent(context, MainActivity::class.java)
                    .putExtras(bundle)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(context, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent, false)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("notice", "入账提醒", NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(234, builder.build())
        }
    }

    private fun openMainActivity(context: Context) {
        val notificationIntent = Intent(context, ChatActivity::class.java)
        notificationIntent.putExtra("test", false)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val main = Intent(context, MainActivity::class.java)
        main.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    }

}