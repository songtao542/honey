package com.snt.phoney.ui.home.friend

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.dip
import kotlinx.android.synthetic.main.fragment_friend_list.*
import kotlinx.android.synthetic.main.fragment_friend_tag.*
import retrofit2.http.Field

/**
 * A fragment representing a list of Items.
 */
class FriendFragment : BaseFragment() {

    lateinit var viewModel: FriendViewModel

    /**
     *  0 默认全部（按时间倒叙）
     *  1 今日新人
     *  2 人气最高
     *  3 距离最近
     *  4 身材最好
     *  5 更多筛选
     *  6 查找城市
     */
    var filterType: FilterType = FilterType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendViewModel::class.java)

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


        newest.setOnClickListener {
            clearFilterSelector()
            newest.isSelected = true
            filterType = FilterType.NEWEST
        }
        popular.setOnClickListener {
            clearFilterSelector()
            popular.isSelected = true
            filterType = FilterType.POPULAR
        }
        nearest.setOnClickListener {
            clearFilterSelector()
            nearest.isSelected = true
            filterType = FilterType.NEAREST
        }
        hottest.setOnClickListener {
            clearFilterSelector()
            hottest.isSelected = true
            filterType = FilterType.HOTTEST
        }

        viewModel.users.observe(this, Observer {

        })


//        viewModel.listUser()
    }

    private fun clearFilterSelector() {
        newest.isSelected = false
        popular.isSelected = false
        nearest.isSelected = false
        hottest.isSelected = false
    }

    private fun listUser() {
        var latitude: String = ""
        var longitude: String = ""
        var type: String = ""
        var page: String = ""
        var city: String = ""
        var heightStart: String = ""
        var heightEnd: String = ""
        var ageStart: String = ""
        var ageEnd: String = ""
        var cupStart: String = ""
        var cupEnd: String = ""
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
