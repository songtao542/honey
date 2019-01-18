package com.snt.phoney.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.snt.phoney.R
import com.snt.phoney.extensions.dip

class ActionButton : LinearLayout {

    private lateinit var imageView: AppCompatImageView
    private lateinit var labelView: AppCompatTextView

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        orientation = LinearLayout.VERTICAL
        imageView = AppCompatImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        val imageLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imageLayoutParams.gravity = Gravity.CENTER
        imageView.layoutParams = imageLayoutParams

        labelView = AppCompatTextView(context)
        labelView.gravity = Gravity.CENTER
        val labelLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        labelLayoutParams.gravity = Gravity.CENTER
        labelView.layoutParams = labelLayoutParams

        addView(imageView)
        addView(labelView)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ActionButton, defStyleAttr, defStyleRes)
            val imageResId = a.getResourceId(R.styleable.ActionButton_actionImage, 0)
            val imageSize = a.getDimensionPixelSize(R.styleable.ActionButton_actionImageSize, dip(70))
            val imageScaleType = a.getInt(R.styleable.ActionButton_actionImageScaleType, 7)
            val imageForegroundResId = a.getResourceId(R.styleable.ActionButton_actionImageForeground, 0)
            val imageBackgroundResId = a.getResourceId(R.styleable.ActionButton_actionImageBackground, 0)
            val labelText = a.getString(R.styleable.ActionButton_actionText)
            val labelTextSize = a.getDimensionPixelSize(R.styleable.ActionButton_actionTextSize, dip(14))
            val labelTextColor = a.getColor(R.styleable.ActionButton_actionTextColor, 0)
            a.recycle()

            imageView.layoutParams = LayoutParams(imageSize, imageSize)

            imageView.scaleType = getScaleType(imageScaleType)

            if (imageResId != 0) {
                imageView.setImageResource(imageResId)
            }

            Log.d("TTTT", "xxxxxxxxxxx imageResId=$imageResId")
            Log.d("TTTT", "xxxxxxxxxxx imageForegroundResId=$imageForegroundResId")
            Log.d("TTTT", "xxxxxxxxxxx imageSize=$imageSize")
            if (imageForegroundResId != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imageView.foreground = context.getDrawable(imageForegroundResId)
                }
            }
            if (imageBackgroundResId != 0) {
                imageView.setBackgroundResource(imageBackgroundResId)
            }
            labelText?.let {
                labelView.text = it
            }
            labelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize.toFloat())
            labelView.setTextColor(labelTextColor)

        }
    }

    private fun getScaleType(scaleType: Int): ImageView.ScaleType {
        return when (scaleType) {
            0 -> ImageView.ScaleType.MATRIX
            1 -> ImageView.ScaleType.FIT_XY
            2 -> ImageView.ScaleType.FIT_START
            3 -> ImageView.ScaleType.FIT_CENTER
            4 -> ImageView.ScaleType.FIT_END
            5 -> ImageView.ScaleType.CENTER
            6 -> ImageView.ScaleType.CENTER_CROP
            else -> ImageView.ScaleType.CENTER_INSIDE
        }
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        imageView.isSelected = selected
        labelView.isSelected = selected
    }

    fun setImageBackgroundResource(resId: Int) {
        imageView.setBackgroundResource(resId)
    }

    fun setImageResource(resId: Int) {
        imageView.setImageResource(resId)
    }

    fun setText(resId: Int) {
        labelView.setText(resId)
    }

    fun setText(text: CharSequence) {
        labelView.text = text
    }

    /**
     * sp
     */
    fun setTextSize(textSize: Float) {
        labelView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
    }

    fun setTextColor(textColor: Int) {
        labelView.setTextColor(textColor)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val event = MotionEvent.obtain(ev)
        event.setLocation((imageView.width / 2).toFloat(), (imageView.height / 2).toFloat())
        imageView.onTouchEvent(event)
        return super.onInterceptTouchEvent(ev)
    }
}