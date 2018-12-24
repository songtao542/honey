package com.snt.phoney.ui.main.square.official

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
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
    private var filterDistance: FilterDistance = FilterDistance.ALL
    private var filterContent: FilterContent = FilterContent.ALL

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_official_recommend_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SquareViewModel::class.java)

        list.layoutManager = LinearLayoutManager(context)
        adapter = OfficialRecommendRecyclerViewAdapter(viewModel, disposeBag)
        list.adapter = adapter

        list.setOnTouchListener { _, _ ->
            if (expandable.isExpanded && !expandable.isAnimating) {
                expandable.collapseWithAnimation()
            }
            return@setOnTouchListener false
        }

        publishTime.setOnClickListener {
            //expandFilter(publishTime, R.array.filter_publish_time)
            popupMenu(publishTime, R.array.filter_publish_time) { position ->
                filterTime = FilterTime.from(position)
                loadDating(true)
            }
        }
        distance.setOnClickListener {
            //expandFilter(distance, R.array.filter_distance)
            popupMenu(distance, R.array.filter_distance) { position ->
                filterDistance = FilterDistance.from(position)
                loadDating(true)
            }
        }
        datingContent.setOnClickListener {
            //expandFilter(datingContent, R.array.filter_dating_content)
            popupMenu(datingContent, R.array.filter_dating_content) { position ->
                filterContent = FilterContent.from(position)
                loadDating(true)
            }
        }

        viewModel.recommendDating.observe(this, Observer {
            adapter.data = it
        })

        list.setLoadMoreListener {
            loadDating(false, it)
        }


        loadDating(true)
    }

    private fun loadDating(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            list.setLoadMoreEnable(true)
        }
        viewModel.listRecommendDating(refresh, filterTime.value, filterDistance.value, filterContent.content, loadMore)
    }

    private fun expandFilter(dropdownLabel: DropdownLabelView, menus: Int) {
        expandable.setIndicatorView(dropdownLabel.getIndicatorView())
        if (expandable.isExpanded) {
            expandable.collapseWithAnimation()
        } else {
            expandable.setFilter(context?.resources?.getStringArray(menus)?.asList())
        }
    }

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
