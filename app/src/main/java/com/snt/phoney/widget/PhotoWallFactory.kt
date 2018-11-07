package com.snt.phoney.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Space
import android.widget.TextView
import com.bumptech.glide.Glide
import com.snt.phoney.R
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.dip

class PhotoWallFactory(private val context: Context) : FlowLayout.ViewFactory {

    private var mUrls: List<String>? = null
    private var mMaxShow = Int.MAX_VALUE
    private var mShowLastAsAdd = true

    fun setUrls(urls: List<String>): PhotoWallFactory {
        this.mUrls = urls
        return this
    }

    fun setMaxShow(max: Int): PhotoWallFactory {
        mMaxShow = max
        return this
    }

    fun setLastAsAdd(lastAsAdd: Boolean): PhotoWallFactory {
        mShowLastAsAdd = lastAsAdd
        return this
    }

    private fun createImageView(url: String): View {
        var imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(context).load(url).into(imageView)
        return imageView
    }

    private fun createLastAdd(): View {
        val add = LinearLayout(context)
        add.setBackgroundColor(context.colorOf(R.color.photo_wall_add))
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
        icon.setImageResource(R.drawable.ic_add)
        icon.scaleType = ImageView.ScaleType.CENTER_INSIDE

        val text = TextView(context)
        val tlp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        tlp.gravity = Gravity.CENTER
        tlp.topMargin = context.dip(10)
        text.layoutParams = tlp
        text.setTextColor(context.colorOf(R.color.photo_wall_add_text))
        text.setText(R.string.add_image)

        add.addView(spaceTop)
        add.addView(icon)
        add.addView(text)
        add.addView(spaceBottom)
        return add
    }

    override fun create(index: Int): View {
        if (mShowLastAsAdd && index == getItemCount() - 1) {
            return createLastAdd()
        }
        return createImageView(mUrls!![index])
    }

    override fun getItemCount(): Int {
        val size = mUrls?.size ?: 0
        val add = if (mShowLastAsAdd) 1 else 0
        return when {
            size == 0 -> add
            size < mMaxShow -> size + add
            else -> mMaxShow
        }
    }

}