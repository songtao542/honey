package com.snt.phoney.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import java.lang.reflect.Field

class RadioButton : AppCompatRadioButton {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var mButtonDrawableField: Field? = null
    private var mButtonDrawable: Drawable? = null

    private fun init() {
//        var mButtonDrawableField = this.javaClass.getDeclaredField("mButtonDrawable")
//        mButtonDrawableField.isAccessible = true
    }

    override fun onDraw(canvas: Canvas?) {
//        if (mButtonDrawable == null) {
//            mButtonDrawable = mButtonDrawableField?.get(this) as? Drawable
//        }
//        mButtonDrawable?.let {
//            it.setBounds(canvas!!.width - it.minimumWidth, 0, width, height)
//        }
        super.onDraw(canvas)


    }
}