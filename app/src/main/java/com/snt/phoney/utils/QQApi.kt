package com.snt.phoney.utils

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.utils.data.Constants
import com.tencent.connect.auth.QQToken
import com.tencent.connect.share.QQShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.tencent.connect.share.QzoneShare
import com.tencent.connect.share.QzonePublish
import android.widget.EditText
import android.widget.LinearLayout
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.snt.phoney.R
import com.amap.api.col.sl3.bm
import java.nio.file.Files.exists
import android.content.Context
import android.os.Environment
import android.util.Log
import com.amap.api.col.sl3.os
import com.snt.phoney.ui.auth.CaptureImageFragment
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception


class QQApi constructor(private val application: Application) : IUiListener {


    private val mTencent: Tencent by lazy { Tencent.createInstance(Constants.Tencent.APP_ID, application) }

    fun login(activity: Activity, listener: IUiListener) {
        mTencent.login(activity, "all", listener)
    }

    fun setAccessToken(accessToken: String, expiresIn: String) {
        mTencent.setAccessToken(accessToken, expiresIn)
    }

    var openId: String?
        get() = mTencent.openId
        set(value) {
            mTencent.openId = value
        }

    val qqToken: QQToken?
        get() = mTencent.qqToken


    fun shareUrl(activity: Activity, title: String, summary: String, url: String, listener: IUiListener? = null) {
        val shareType = QQShare.SHARE_TO_QQ_TYPE_APP
        val params = Bundle()
        val flag = QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN or QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title)
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url)
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary)
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "纯蜜")
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType)
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, flag)

        if (listener == null) {
            mTencent.shareToQQ(activity, params, this)
        } else {
            mTencent.shareToQQ(activity, params, listener)
        }
    }


    fun shareQzoneUrl(activity: Activity, title: String, summary: String, url: String, listener: IUiListener? = null) {
        GlobalScope.launch(Dispatchers.Main) {
            //val shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT
            val shareType = QzoneShare.SHARE_TO_QZONE_TYPE_APP
            val params = Bundle()
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType)
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title)
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary)
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url)
            val imageUrls = ArrayList<String>()
            withContext(Dispatchers.Default) {
                saveLauncherBitmap(application)?.let {
                    imageUrls.add(it)
                }
                GlobalScope.launch(Dispatchers.Main) {
                    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls)
                    if (listener == null) {
                        mTencent.shareToQzone(activity, params, this@QQApi)
                    } else {
                        mTencent.shareToQzone(activity, params, listener)
                    }
                }
                return@withContext
            }
        }
    }


    private fun saveLauncherBitmap(context: Context): String? {
        var file: File? = null
        try {
            file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ic_launcher.jpg")
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            Log.e("QQApi", "open file error:${e.message}")
        }
        var out: OutputStream? = null
        try {
            out = FileOutputStream(file)
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_for_share)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            return file?.absolutePath
        } catch (e: IOException) {
            Log.e("QQApi", "Cannot write to $file", e)
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    Log.e("QQApi", "error: ${e.message}")
                }
            }
        }
        return null
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, listener: IUiListener) {
        Tencent.onActivityResultData(requestCode, resultCode, data, listener)
    }


    override fun onComplete(p0: Any?) {
    }

    override fun onCancel() {
    }

    override fun onError(p0: UiError?) {
    }
}