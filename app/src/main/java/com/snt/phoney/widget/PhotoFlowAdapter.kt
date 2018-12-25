package com.snt.phoney.widget

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.forEach
import java.util.*

class PhotoFlowAdapter(private val context: Context) : FlowLayout.ViewAdapter {


    enum class AddButtonStyle {
        BORDER,
        SOLID;
    }

    private var mUrls: List<String>? = null
    private var mUris: List<Uri>? = null
    private var mPhotos: List<Photo>? = null
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

    fun setPhotos(photos: List<Photo>): PhotoFlowAdapter {
        this.mPhotos = photos
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

    private fun createImageView(res: Int, price: Int): View {
        val view = FrameLayout(context)
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setBackgroundResource(R.drawable.ic_red_envelope_photo_border)
        imageView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        view.addView(imageView)

        val textView = TextView(context)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        textView.setTextColor(context.colorOf(R.color.white))
        textView.text = context.getString(R.string.pay_mibi_template, price.toString())
        val dip5 = context.dip(5)
        textView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            bottomMargin = dip5
        }
        val dip4 = context.dip(4)
        textView.setPadding(dip5 * 2, dip4, dip5 * 2, dip4)
        textView.setBackgroundResource(R.drawable.button_primary_circle_corner_selector)
        view.addView(textView)

        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        Glide.with(context).applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_placeholder)).load(res).into(imageView)
        return view
    }


    private fun createImageView(url: String): View {
        val imageView = if (cache.size > 0) cache.pop() else ImageView(context) //ImageView(context)//
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(context).applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_placeholder)).load(url).into(imageView)
        return imageView
    }

    private fun createImageView(uri: Uri): View {
        val imageView = if (cache.size > 0) cache.pop() else ImageView(context) //ImageView(context)//
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(context).load(uri).into(imageView)
        return imageView
    }

    private fun createLastAdd(): View {
        cachedAdd?.let {
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
            val uri = mUris!![index]
            createImageView(uri).apply { setTag(R.id.tag, uri) }
        } else if (mUrls != null) {
            val url = mUrls!![index]
            createImageView(url).apply { setTag(R.id.tag, url) }
        } else {
            val photo = mPhotos!![index]
            val path = photo.path
            if (TextUtils.isEmpty(path)) {
                createImageView(R.drawable.ic_red_envelopes, photo.price).apply { setTag(R.id.tag, photo) }
            } else {
                createImageView(photo.path!!).apply { setTag(R.id.tag, photo) }
            }
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
        val size = mUris?.size ?: mUrls?.size ?: mPhotos?.size ?: 0
        val add = if (mShowLastAsAdd) 1 else 0
        return when {
            size == 0 -> add
            size < mMaxShow -> size + add
            else -> mMaxShow
        }
    }

}