package com.snt.phoney.widget.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonospacedItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val halfSpace = space / 2
        if (parent.paddingLeft != halfSpace || parent.paddingRight != halfSpace) {
            parent.clipToPadding = false
            parent.setPadding(halfSpace, parent.paddingTop, halfSpace, parent.paddingBottom)
        }
        var params = view.layoutParams
        if (params is GridLayoutManager.LayoutParams) {
            outRect.left = halfSpace
            outRect.right = halfSpace
            outRect.top = halfSpace
            outRect.bottom = halfSpace

//            //spanSize 表示宽度占用几个列
//            var spanSize = params.spanSize
//            //spanIndex 表示第几列
//            var spanIndex = params.spanIndex
//            var spanCount = (parent.layoutManager as GridLayoutManager).spanCount
//            //var position = params.viewLayoutPosition
//            if (spanSize == 1 && spanIndex == 0) {
//                outRect.left = halfSpace
//                outRect.right = halfSpace
//                outRect.top = halfSpace
//                outRect.bottom = halfSpace
//            } else if (spanSize == 1 && spanIndex == spanCount - 1) {
//                outRect.left = halfSpace
//                outRect.right = halfSpace
//                outRect.top = halfSpace
//                outRect.bottom = halfSpace
//            } else {
//                outRect.left = halfSpace
//                outRect.right = halfSpace
//                outRect.top = halfSpace
//                outRect.bottom = halfSpace
//            }
        }
    }
}