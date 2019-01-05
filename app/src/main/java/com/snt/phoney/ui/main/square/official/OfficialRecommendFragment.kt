package com.snt.phoney.ui.main.square.official

import android.Manifest
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.setLoadMoreEnable
import com.snt.phoney.extensions.setLoadMoreListener
import com.snt.phoney.ui.main.square.SquareViewModel
import com.snt.phoney.widget.DropdownLabelView
import com.snt.phoney.widget.PopupList
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.fragment_official_recommend_list.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class OfficialRecommendFragment : BaseFragment() {

    lateinit var viewModel: SquareViewModel

    lateinit var adapter: OfficialRecommendRecyclerViewAdapter

    private var filterTime: FilterTime = FilterTime.ALL
    private var filterDistance: FilterDistance = FilterDistance.NONE
    private var filterContent: FilterContent = FilterContent.NONE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_official_recommend_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SquareViewModel::class.java)

        list.layoutManager = LinearLayoutManager(context)
        adapter = OfficialRecommendRecyclerViewAdapter(this, viewModel)
        list.adapter = adapter

//        list.setOnTouchListener { _, _ ->
//            if (expandable.isExpanded && !expandable.isAnimating) {
//                expandable.collapseWithAnimation()
//            }
//            return@setOnTouchListener false
//        }

        publishTime.setOnClickListener {
            //expandFilter(publishTime, R.array.filter_publish_time)
            popupMenu(publishTime, R.array.filter_publish_time) { position ->
                filterDistance = FilterDistance.NONE
                filterContent = FilterContent.NONE
                filterTime = FilterTime.from(position)
                loadDating(true)
            }
        }
        distance.setOnClickListener {
            //expandFilter(distance, R.array.filter_distance)
            popupMenu(distance, R.array.filter_distance) { position ->
                filterTime = FilterTime.NONE
                filterContent = FilterContent.NONE
                filterDistance = FilterDistance.from(position)
                if (checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    loadDating(true)
                }
            }
        }
        datingContent.setOnClickListener {
            //expandFilter(datingContent, R.array.filter_dating_content)
            popupMenu(datingContent, R.array.filter_dating_content) { position ->
                filterTime = FilterTime.NONE
                filterDistance = FilterDistance.NONE
                filterContent = FilterContent.from(position)
                loadDating(true)
            }
        }

        viewModel.success.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            adapter.notifyDataSetChanged()
        })

        viewModel.error.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.recommendDating.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            adapter.data = it
        })

        openLocation.setOnClickListener {
            checkAndRequestPermission(*getPermissions())
        }

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            loadDating(true)
        }

        list.setLoadMoreListener {
            loadDating(false, it)
        }

        //loadDating(true)
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onResume() {
        super.onResume()
        if (checkAppPermission(*getPermissions())) {
            openLocationLayout.visibility = View.GONE
            if (viewModel.isRecommendListEmpty()) {
                loadDating(true)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (checkAppPermission(*getPermissions())) {
            openLocationLayout.visibility = View.GONE
            loadDating(true)
        } else {
            filterDistance = FilterDistance.from(-1)
        }
    }

    private fun loadDating(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (!checkAppPermission(*getPermissions())) {
            openLocationLayout.visibility = View.VISIBLE
            return
        }
        if (refresh) {
            list.setLoadMoreEnable(true)
        }
        val timeFilter = filterTime.toString()
        val distanceFilter = filterDistance.toString()
        val contentFilter = filterContent.toString()
        viewModel.listRecommendDating(refresh, timeFilter, distanceFilter, contentFilter, loadMore)

    }

//    private fun expandFilter(dropdownLabel: DropdownLabelView, menus: Int) {
//        expandable.setIndicatorView(dropdownLabel.getIndicatorView())
//        if (expandable.isExpanded) {
//            expandable.collapseWithAnimation()
//        } else {
//            expandable.setFilter(context?.resources?.getStringArray(menus)?.asList())
//        }
//    }

    private fun popupMenu(dropdownLabel: DropdownLabelView, menus: Int, handler: ((selectPosition: Int) -> Unit)) {
        val menuList = context!!.resources.getStringArray(menus).asList()
        val list = PopupList(context, menuList)
        list.setWidth(false, anchor.width)
        list.setPosition(0)
        list.setItemHeight(50, menuList.size)
        list.setTextGravity(Gravity.CENTER)

        list.setOnDismissListener {
            dropdownLabel.collapseIndicator()
        }
        list.setOnMenuItemClickListener {
            dropdownLabel.setText(it.title)
            handler.invoke(it.position)
        }
        dropdownLabel.expandIndicator()
        list.show(anchor, Gravity.BOTTOM)
    }

    companion object {

        @JvmStatic
        fun newInstance() = OfficialRecommendFragment()
    }
}
