package com.snt.phoney.ui.main.home.friend

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.*
import com.snt.phoney.widget.itemdecoration.MonospacedItemDecoration
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.fragment_friend_list.*
import kotlinx.android.synthetic.main.fragment_friend_tag.*

/**
 * A fragment representing a list of Items.
 */
class FriendFragment : BaseFragment() {

    private lateinit var viewModel: FriendViewModel

    /**
     *  0 默认全部（按时间倒叙）
     *  1 今日新人
     *  2 人气最高
     *  3 距离最近
     *  4 身材最好
     *  5 更多筛选
     *  6 查找城市
     */
    private var filterType: FilterType = FilterType.DEFAULT

    private var city: String = ""
    private var heightStart: String = ""
    private var heightEnd: String = ""
    private var ageStart: String = ""
    private var ageEnd: String = ""
    private var cupStart: String = ""
    private var cupEnd: String = ""

    private var adapter: FriendRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendViewModel::class.java)

        adapter = FriendRecyclerViewAdapter()
        list.addItemDecoration(MonospacedItemDecoration(dip(8)))
        list.layoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0 -> 1
                        else -> 1
                    }
                }
            }
        }
        list.adapter = adapter

        moreTag.setOnClickListener {
            val fragment = FilterFragment.newInstance()
            fragment.setOnResultListener { result ->
                heightStart = result.startHeight.toString()
                heightEnd = result.endHeight.toString()
                ageStart = result.startAge.toString()
                ageEnd = result.endAge.toString()
                cupStart = result.startCup
                cupEnd = result.endCup
                load(true)
            }
            activity?.addFragmentSafely(android.R.id.content, fragment, "filter", true,
                    enterAnimation = R.anim.slide_in_right, popExitAnimation = R.anim.slide_out_right)
        }

        newest.setOnClickListener {
            filter(FilterType.NEWEST, newest)
        }
        popular.setOnClickListener {
            filter(FilterType.POPULAR, popular)
        }
        nearest.setOnClickListener {
            if (checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                filter(FilterType.NEAREST, nearest)
            }
        }
        hottest.setOnClickListener {
            filter(FilterType.HOTTEST, hottest)
        }

        viewModel.users.observe(this, Observer {
            adapter?.data = it
        })

        list.setLoadMoreListener {
            load(false, it)
        }

        load(true)
    }

    private fun checkPermission(): Boolean {
        return context?.checkAppPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) == true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (checkPermission()) {
            filter(FilterType.NEAREST, nearest)
        }
    }

    private fun filter(type: FilterType, view: TextView) {
        clearFilterSelector()
        if (filterType == type) {
            view.isSelected = false
            filterType = FilterType.DEFAULT
        } else {
            view.isSelected = true
            filterType = type
        }
        load(true)
    }

    private fun clearFilterSelector() {
        newest.isSelected = false
        popular.isSelected = false
        nearest.isSelected = false
        hottest.isSelected = false
    }

    private fun load(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            list.setLoadMoreEnable(true)
        }
        viewModel.listUser(refresh, filterType, city, heightStart, heightEnd,
                ageStart, ageEnd, cupStart, cupEnd, loadMore)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FriendFragment()
    }


}
