package com.snt.phoney.utils.data

import android.util.Log
import com.snt.phoney.extensions.TAG
import com.snt.phoney.BuildConfig
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5 {
    @JvmStatic
    fun md5(string: String): String {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "to sign string: $string")
        }
        try {
            //获取md5加密对象
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            //对字符串加密，返回字节数组
            val digest: ByteArray = instance.digest(string.toByteArray())
            var sb = StringBuilder()
            for (b in digest) {
                //获取低八位有效值
                var i: Int = b.toInt() and 0xff
                //将整数转化为16进制
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    //如果是一位的话，补0
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            val sign = sb.toString()
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "signed string : $sign")
            }
            return sign
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}