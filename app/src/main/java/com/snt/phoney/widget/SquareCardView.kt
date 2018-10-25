package com.snt.phoney.widget

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v7.cardview.R
import android.support.v7.widget.CardView
import android.util.AttributeSet

class SquareCardView : CardView {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.cardViewStyle)

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}