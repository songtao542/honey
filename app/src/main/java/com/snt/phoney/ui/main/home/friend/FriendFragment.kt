package com.snt.phoney.ui.main.home.friend

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
import com.snt.phoney.extensions.dip
import com.snt.phoney.widget.itemdecoration.MonospacedItemDecoration
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

        newest.setOnClickListener {
            filter(FilterType.NEWEST, newest)
        }
        popular.setOnClickListener {
            filter(FilterType.POPULAR, popular)
        }
        nearest.setOnClickListener {
            filter(FilterType.NEAREST, nearest)
        }
        hottest.setOnClickListener {
            filter(FilterType.HOTTEST, hottest)
        }

        viewModel.users.observe(this, Observer {
            adapter?.data = it
        })

        listUser()
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
        listUser()
    }

    private fun clearFilterSelector() {
        newest.isSelected = false
        popular.isSelected = false
        nearest.isSelected = false
        hottest.isSelected = false
    }

    private fun listUser() {
        viewModel.listUser(filterType, city, heightStart, heightEnd,
                ageStart, ageEnd, cupStart, cupEnd)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FriendFragment()
    }



}
