package com.snt.phoney.widget

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Space
import android.widget.TextView
import com.bumptech.glide.Glide
import com.snt.phoney.R
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.dip
import java.util.*

class PhotoFlowAdapter(private val context: Context) : FlowLayout.ViewAdapter {


    enum class AddButtonStyle {
        BORDER,
        SOLID;
    }

    private var mUrls: List<String>? = null
    private var mUris: List<Uri>? = null
    private var mMaxShow = Int.MAX_VALUE
    private var mShowLastAsAdd = true
    private var mShowAddWhenFull = true
    private var addStyle: AddButtonStyle = AddButtonStyle.SOLID

    private var mOnAddClickListener: ((size: Int) -> Unit)? = null

    fun setOnAddClickListener(listener: (size: Int) -> Unit): PhotoFlowAdapter {
        mOnAddClickListener = listener
        return this
    }

    fun setUrls(urls: List<String>): PhotoFlowAdapter {
        this.mUrls = urls
        return this
    }

    fun setUris(uris: List<Uri>): PhotoFlowAdapter {
        this.mUris = uris
        return this
    }

    fun setMaxShow(max: Int): PhotoFlowAdapter {
        mMaxShow = max
        return this
    }

    fun setAddButtonStyle(style: AddButtonStyle): PhotoFlowAdapter {
        this.addStyle = style
        return this
    }

    fun setShowAddWhenFull(show: Boolean): PhotoFlowAdapter {
        this.mShowAddWhenFull = show
        return this
    }

    fun setLastAsAdd(lastAsAdd: Boolean): PhotoFlowAdapter {
        mShowLastAsAdd = lastAsAdd
        return this
    }

    private val cache = ArrayDeque<ImageView>()
    private var cachedAdd: View? = null

    private fun createImageView(url: String): View {
        var imageView = if (cache.size > 0) cache.pop() else ImageView(context) //ImageView(context)//
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(context).load(url).into(imageView)
        return imageView
    }

    private fun createImageView(uri: Uri): View {
        var imageView = if (cache.size > 0) cache.pop() else ImageView(context) //ImageView(context)//
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(context).load(uri).into(imageView)
        return imageView
    }

    private fun createLastAdd(): View {
        cachedAdd?.let {
            val alp = it.layoutParams
            if (alp is ViewGroup.MarginLayoutParams) {
                alp.rightMargin = 0
            }
            cachedAdd!!.layoutParams = alp
            return cachedAdd!!
        }
        val add = LinearLayout(context)
        if (addStyle == AddButtonStyle.SOLID) {
            add.setBackgroundColor(context.colorOf(R.color.photo_wall_add))
        } else {
            add.setBackgroundResource(R.drawable.add_button_border)
        }
        add.orientation = LinearLayout.VERTICAL

        val spaceTop = Space(context)
        val toplp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0)
        toplp.weight = 1f
        spaceTop.layoutParams = toplp

        val spaceBottom = Space(context)
        val bottomlp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0)
        bottomlp.weight = 1f
        spaceBottom.layoutParams = bottomlp

        val icon = ImageView(context)
        val ilp = LinearLayout.LayoutParams(context.dip(20), context.dip(20))
        ilp.gravity = Gravity.CENTER
        icon.layoutParams = ilp
        if (addStyle == AddButtonStyle.SOLID) {
            icon.setImageResource(R.drawable.ic_add)
        } else {
            icon.setImageResource(R.drawable.ic_add_darker)
        }
        icon.scaleType = ImageView.ScaleType.CENTER_INSIDE

        val text = TextView(context)
        val tlp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        tlp.gravity = Gravity.CENTER
        tlp.topMargin = context.dip(10)
        text.layoutParams = tlp
        if (addStyle == AddButtonStyle.SOLID) {
            text.setTextColor(context.colorOf(R.color.photo_wall_add_text))
        } else {
            text.setTextColor(context.colorOf(R.color.darker))
        }
        text.setText(R.string.add_image)

        add.addView(spaceTop)
        add.addView(icon)
        add.addView(text)
        add.addView(spaceBottom)
        if (mOnAddClickListener != null) {
            add.setOnClickListener { mOnAddClickListener?.invoke(getItemCount()) }
        }
        return add
    }

    override fun create(index: Int): View {
        if (mShowLastAsAdd && index == getItemCount() - 1) {
            val size = mUris?.size ?: mUrls?.size ?: 0
            if (size < mMaxShow || (size >= mMaxShow && mShowAddWhenFull)) {
                return createLastAdd()
            }
        }
        return if (mUris != null) {
            createImageView(mUris!![index])
        } else {
            createImageView(mUrls!![index])
        }
    }

    override fun onRecycleView(view: View) {
        if (view is ImageView) {
            cache.add(view)
        } else {
            cachedAdd = view
        }
    }

    override fun getItemCount(): Int {
        val size = mUris?.size ?: mUrls?.size ?: 0
        val add = if (mShowLastAsAdd) 1 else 0
        return when {
            size == 0 -> add
            size < mMaxShow -> size + add
            else -> mMaxShow
        }
    }

}