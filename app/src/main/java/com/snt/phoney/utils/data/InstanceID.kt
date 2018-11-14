package com.snt.phoney.utils.data

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.security.MessageDigest

object InstanceID {

    fun getId(context: Context): String {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        //设备序列号（Serial Number, SN）
        val serialNum = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Build.getSerial()
            ""
        } else {
            Build.SERIAL
        }
        val fingerprint = Build.FINGERPRINT
        return md5("$androidId$fingerprint")
    }


    private fun md5(str: String): String {
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val digest = md5.digest(str.toByteArray())
            val hexValue = StringBuilder()
            for (b in digest) {
                val v = b.toInt() and 0xff
                if (v < 16) {
                    hexValue.append("0")
                }
                hexValue.append(Integer.toHexString(v))
            }
            return hexValue.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}