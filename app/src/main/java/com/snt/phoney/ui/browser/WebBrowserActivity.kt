package com.snt.phoney.ui.browser

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.*
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.snt.phoney.R
import com.snt.phoney.base.BaseNoViewModelActivity
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.widget.PopupList
import kotlinx.android.synthetic.main.activity_web_browser.*
import java.util.regex.Pattern

/**
 *
 *  * getIntent().getExtras().getParcelable("article");
 *  * getIntent().getExtras().getString("url");
 *  * getIntent().getExtras().getString("title");
 *
 *
 * @author songtao
 */
class WebBrowserActivity : BaseNoViewModelActivity(), View.OnClickListener {
    private var settings: WebSettings? = null
    private var url = ""

    private var textSize = 120
    private lateinit var popupMenu: PopupList

    private val onMenuItemClickListener = PopupList.OnMenuItemClickListener { item ->
        when (item.position) {
            0 -> {
            }
            1 -> fontsizeLayout.visibility = View.VISIBLE
            else -> {
            }
        }
    }

    override fun onConfigureTheme() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setLayoutFullscreen()
        setContentView(R.layout.activity_web_browser)
        init()
        val settings = webview.settings
        settings.javaScriptEnabled = true
        // settings.setTextZoom(75);
        webview.addJavascriptInterface(InJavaScriptLocalObj(), "local_obj")

        webview.webViewClient = object : WebViewClient() {

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                webview.loadUrl("file:///android_asset/failed.html")
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if ("file:///android_asset/failed.html" == url) {
                } else {
                }
            }
        }


        val url = intent.extras?.getString("url")
        val title = intent.extras?.getString("title")
        if (!TextUtils.isEmpty(title)) {
            toolbar.title = title
        }
        if (url != null) {
            this.url = url
            // 设置可以支持缩放
            webview.settings.setSupportZoom(true)
            // 设置出现缩放工具
            webview.settings.builtInZoomControls = true
            // 扩大比例的缩放
            webview.settings.useWideViewPort = true
            // // 自适应屏幕
            // webView.getSettings().setLayoutAlgorithm(
            // LayoutAlgorithm.SINGLE_COLUMN);
            webview.settings.loadWithOverviewMode = true
            webview.loadUrl(url)
        }
    }

    override fun onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            super.onBackPressed()
        }
    }

    internal inner class InJavaScriptLocalObj {
        @Suppress("unused")
        @JavascriptInterface
        fun showSource(html: String) {
        }
    }

    @Suppress("unused")
    fun findByRegex(content: String, reg: String): String? {
        val pattern = Pattern.compile(reg)
        val macther = pattern.matcher(content)
        return if (macther.find()) macther.group() else null
    }

    private fun init() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
        textZoomLabel.text = "120"
        cancel.setOnClickListener { fontsizeLayout.visibility = View.INVISIBLE }

        settings = webview.settings
        settings?.setSupportZoom(true)
        settings?.textZoom = textSize

        popupMenu = PopupList(this)
        popupMenu.setWidth(150)
        popupMenu.setBackgroundColor(-0xb2b2b3)
        popupMenu.setTextColor(-0x1)
        popupMenu.setDivider(ColorDrawable(-0x969697))
        // popupMenu.add(R.drawable.news_share_icon, "分享给好友");
        // popupMenu.add(R.drawable.news_font_size_icon, "调整字体");
        popupMenu.add("分享给好友")
        popupMenu.add("调整字体")
        popupMenu.setOnMenuItemClickListener(onMenuItemClickListener)

        textZoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(arg0: SeekBar, progress: Int, fromUser: Boolean) {
                textSize = progress + 80
                settings!!.textZoom = textSize
                textZoomLabel.text = "" + textSize
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onClick(v: View) {

    }
}
