package com.snt.phoney.ui.home.friend

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.dip
import kotlinx.android.synthetic.main.fragment_friend_list.*

/**
 * A fragment representing a list of Items.
 */
class FriendFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(list) {
            addItemDecoration(ItemDecoration(dip(8)))
            val gridLayoutManager = GridLayoutManager(context, 2)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0 -> 1
                        else -> 1
                    }
                }
            }
            layoutManager = gridLayoutManager
            adapter = FriendRecyclerViewAdapter()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }


    companion object {
        @JvmStatic
        fun newInstance() = FriendFragment()
    }


    inner class ItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            var params = view.layoutParams
            if (params is GridLayoutManager.LayoutParams) {
                var spanSize = params.spanSize
                var spanIndex = params.spanIndex
                var position = params.viewLayoutPosition
                if (spanSize == 1 && spanIndex == 0) {
                    outRect.left = space
                    outRect.right = space / 2
                    outRect.top = space / 2
                    outRect.bottom = space / 2
                } else if (spanSize == 1 && spanIndex == 1) {
                    outRect.left = space / 2
                    outRect.right = space
                    outRect.top = space / 2
                    outRect.bottom = space / 2
                }
            }
        }
    }
}
