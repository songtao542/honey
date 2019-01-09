package com.snt.phoney.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.multidex.MultiDex
import cn.jpush.android.api.JPushInterface
import cn.jpush.im.android.api.JMessageClient
import com.snt.phoney.BuildConfig
import com.snt.phoney.di.AppInjector
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.service.VoiceCallEngine
import com.snt.phoney.utils.data.Constants
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import jiguang.chat.utils.NotificationClickEventReceiver
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import javax.inject.Inject


class App : Application(), HasActivityInjector
/**, HasServiceInjector **/
{

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    //@Inject
    //lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    @Suppress("unused")
    @Inject
    lateinit var userAccessor: UserAccessor

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)
        initJPush()
        initJMessage()
        initBugly()
        initUM()
        initJMRtc()
        //startVoiceCallService()
    }

    override fun activityInjector() = dispatchingActivityInjector

    //override fun serviceInjector() = dispatchingServiceInjector

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initUM() {
        if (BuildConfig.DEBUG) {
            UMConfigure.setLogEnabled(true)
        }
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
        MobclickAgent.openActivityDurationTrack(false)
    }

    private fun initJMessage() {
        if (BuildConfig.DEBUG) {
            JMessageClient.setDebugMode(true)
        } else {
            JMessageClient.setDebugMode(false)
        }
        JMessageClient.init(this)
        //设置Notification的模式
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND or JMessageClient.FLAG_NOTIFY_WITH_LED or JMessageClient.FLAG_NOTIFY_WITH_VIBRATE)
        JMessageClient.registerEventReceiver(NotificationClickEventReceiver(this))
    }

    private fun initJPush() {
        if (BuildConfig.DEBUG) {
            JPushInterface.setDebugMode(true)
        } else {
            JPushInterface.setDebugMode(false)
        }
        JPushInterface.init(this)
    }

    private fun initJMRtc() {
        VoiceCallEngine.init(this)
    }

    //private fun startVoiceCallService() {
    //startService(Intent(this, VoiceCallService::class.java))
    //}

    private fun initBugly() {
        val context = applicationContext
        // 获取当前包名
        val packageName = context.packageName
        // 获取当前进程名
        val processName = getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        // 初始化Bugly, 测试阶段建议设置成true，发布时设置为false
        if (BuildConfig.DEBUG) {
            CrashReport.initCrashReport(context, Constants.Bugly.APP_ID, true, strategy)
        } else {
            CrashReport.initCrashReport(context, Constants.Bugly.APP_ID, false, strategy)
        }

    }

    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader!!.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader!!.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }

}