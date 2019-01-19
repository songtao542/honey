package com.snt.phoney.widget

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.domain.model.Photo
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
    private var mPhotos: List<Photo>? = null
    private var mMaxShow = Int.MAX_VALUE
    private var mShowLastAsAdd = true
    private var mShowAddWhenFull = true
    private var addStyle: AddButtonStyle = AddButtonStyle.SOLID

    private var viewerIsMember = false

    private var mOnAddClickListener: ((size: Int) -> Unit)? = null

    fun setOnAddClickListener(listener: (size: Int) -> Unit): PhotoFlowAdapter {
        mOnAddClickListener = listener
        return this
    }

    fun setUrls(urls: List<String>): PhotoFlowAdapter {
        this.mUrls = urls
        return this
    }

    fun setViewerIsMember(isMember: Boolean): PhotoFlowAdapter {
        this.viewerIsMember = isMember
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

    private val cache = ArrayDeque<View>()
    private var cachedAdd: View? = null

    private fun createImageView(price: Int, url: String? = null): View {
        val view = TextImageView(context)
        if (price > 0) {
            view.setTextPrimaryStyle(true)
            view.setImageResource(R.drawable.ic_red_envelopes)
            view.setImageBackgroundResource(R.drawable.ic_red_envelope_photo_border)
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            view.setTextColor(context.colorOf(R.color.white))
            view.setText(context.getString(R.string.pay_mibi_template, price.toString()))
        } else if (!TextUtils.isEmpty(url)) {
            view.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(context).applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_placeholder)).load(url).into(view.imageView)
        } else {
            view.setImageResource(R.drawable.ic_placeholder)
            view.setImageBackgroundResource(R.drawable.ic_red_envelope_photo_border)
        }
        return view
    }

    @Suppress("LiftReturnOrAssignment")
    private fun createImageView(photo: Photo): View {
        if (photo.flag == 1) {
            if (!photo.paid) {
                return createImageView(photo.price)
            } else {
                return createImageView(price = 0, url = photo.path ?: "")
            }
        } else {
            var view = if (cache.size > 0) cache.pop() else TextImageView(context)
            if (view !is TextImageView) {
                view = TextImageView(context)
            }
            val imageView = view.imageView
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            //if (viewerIsMember) {
            if (photo.burn == 1) {
                view.setTextVisibility(View.VISIBLE)
                view.setTextPrimaryStyle(false)
                view.setText(R.string.photo_has_burned)
                imageView.setImageResource(R.drawable.ic_burn_placeholder)
            } else {
                if (photo.path?.contains("image-placeholder.png") == true) {
                    imageView.setImageResource(R.drawable.ic_burn_placeholder)
                } else {
                    view.setTextVisibility(View.INVISIBLE)
                    Glide.with(context)
                            .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_placeholder))
                            .load(photo.path ?: "")
                            .into(imageView)
                }
            }
            //} else {
            //    Glide.with(context).load(R.drawable.ic_burn_placeholder).into(imageView)
            //}
            return view
        }
    }

    private fun createImageView(url: String): View {
        val view = if (cache.size > 0) cache.pop() else AppCompatImageView(context)
        val imageView = if (view is TextImageView) {
            view.setTextVisibility(View.INVISIBLE)
            view.imageView
        } else view as ImageView
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(context).applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_placeholder)).load(url).into(imageView)
        return view
    }

    private fun createImageView(uri: Uri): View {
        val view = if (cache.size > 0) cache.pop() else AppCompatImageView(context)
        val imageView = if (view is TextImageView) {
            view.setTextVisibility(View.INVISIBLE)
            view.imageView
        } else view as ImageView
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(context).load(uri).into(imageView)
        return view
    }

    private fun createLastAdd(): View {
        cachedAdd?.let {
            return cachedAdd!!
        }
        val add = AddIconView(context, addStyle)
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
        return when {
            mUris != null -> {
                val uri = mUris!![index]
                createImageView(uri).apply { setTag(R.id.tag, uri) }
            }
            mUrls != null -> {
                val url = mUrls!![index]
                createImageView(url).apply { setTag(R.id.tag, url) }
            }
            else -> {
                val photo = mPhotos!![index]
                createImageView(photo).apply { setTag(R.id.tag, photo) }
            }
        }
    }

    override fun onRecycleView(view: View) {
        when (view) {
            is AddIconView -> cachedAdd = view
            else -> cache.add(view)
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

    inner class TextImageView(context: Context) : FrameLayout(context) {

        val imageView: AppCompatImageView = AppCompatImageView(context)
        private val textView: AppCompatTextView = AppCompatTextView(context)

        init {
            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            imageView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            addView(imageView)

            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            textView.setTextColor(context.colorOf(R.color.white))

            val dip5 = context.dip(5)
            textView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                bottomMargin = dip5
            }
            addView(textView)
        }

        fun setTextPrimaryStyle(primary: Boolean) {
            if (primary) {
                val dip5 = context.dip(5)
                val dip4 = context.dip(4)
                textView.setPadding(dip5 * 2, dip4, dip5 * 2, dip4)
                textView.setBackgroundResource(R.drawable.button_primary_circle_corner_selector)
            } else {
                textView.setPadding(0, 0, 0, 0)
                textView.setBackgroundColor(context.colorOf(android.R.color.transparent))
            }
        }

        fun setTextVisibility(visibility: Int) {
            textView.visibility = visibility
        }

        fun setText(resId: Int) {
            textView.setText(resId)
        }

        fun setText(text: CharSequence) {
            textView.text = text
        }

        fun setTextSize(unit: Int, size: Float) {
            textView.setTextSize(unit, size)
        }

        fun setTextColor(color: Int) {
            textView.setTextColor(color)
        }

        @Suppress("unused")
        fun setTextBackgroundResource(resId: Int) {
            textView.setBackgroundResource(resId)
        }

        fun setImageResource(resId: Int) {
            imageView.setImageResource(resId)
        }

        fun setImageBackgroundResource(resId: Int) {
            imageView.setBackgroundResource(resId)
        }
    }

    inner class AddIconView(context: Context, addStyle: AddButtonStyle) : LinearLayout(context) {
        init {
            if (addStyle == AddButtonStyle.SOLID) {
                setBackgroundColor(context.colorOf(R.color.photo_wall_add))
            } else {
                setBackgroundResource(R.drawable.add_button_border)
            }
            orientation = LinearLayout.VERTICAL

            val spaceTop = Space(context)
            val toplp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
            toplp.weight = 1f
            spaceTop.layoutParams = toplp

            val spaceBottom = Space(context)
            val bottomlp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
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
            val tlp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            tlp.gravity = Gravity.CENTER
            tlp.topMargin = context.dip(10)
            text.layoutParams = tlp
            if (addStyle == AddButtonStyle.SOLID) {
                text.setTextColor(context.colorOf(R.color.photo_wall_add_text))
            } else {
                text.setTextColor(context.colorOf(R.color.darker))
            }
            text.setText(R.string.add_image)

            addView(spaceTop)
            addView(icon)
            addView(text)
            addView(spaceBottom)
        }

    }

}