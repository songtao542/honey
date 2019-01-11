package com.snt.phoney.ui.main.home.friend

import android.Manifest
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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

    private var heightStart: String = ""
    private var heightEnd: String = ""
    private var ageStart: String = ""
    private var ageEnd: String = ""
    private var weightStart: String = ""
    private var weightEnd: String = ""
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
                filterType = FilterType.MORE
                heightStart = result.startHeight.toString()
                heightEnd = result.endHeight.toString()
                ageStart = result.startAge.toString()
                ageEnd = result.endAge.toString()
                weightStart = result.startWeight.toString()
                weightEnd = result.endWeight.toString()
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
            swipeRefresh.isRefreshing = false
            adapter?.data = it
        })

        viewModel.error.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            if (!isHidden || !userVisibleHint) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.cityFilter.observe(this, Observer {
            if (!TextUtils.isEmpty(it)) {
                filter(FilterType.BYCITY)
            } else {
                filter(FilterType.DEFAULT)
            }
        })
        swipeRefresh.setProgressViewOffset(false, -dip(40), dip(8))
        swipeRefresh.setSlingshotDistance(dip(64))
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            load(true, list.loadMore)
        }

        list.setLoadMoreListener {
            load(false, it)
        }

        load(true, list.loadMore)
    }

    private fun checkPermission(): Boolean {
        return context?.checkAppPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) == true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (checkPermission()) {
            filter(FilterType.NEAREST, nearest)
        }
    }

    private fun filter(type: FilterType, view: TextView? = null) {
        clearFilterSelector()
        if (view != null) {
            if (filterType == type) {
                view.isSelected = false
                filterType = FilterType.DEFAULT
            } else {
                view.isSelected = true
                filterType = type
            }
        } else {
            filterType = type
        }
        load(true, list.loadMore)
    }

    private fun clearFilterSelector() {
        newest.isSelected = false
        popular.isSelected = false
        nearest.isSelected = false
        hottest.isSelected = false
    }

    private fun load(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            swipeRefresh.isRefreshing = true
            list.setLoadMoreEnable(true)
        }
        viewModel.listUser(refresh, filterType, heightStart, heightEnd,
                ageStart, ageEnd, cupStart, cupEnd, loadMore)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FriendFragment()
    }


}
