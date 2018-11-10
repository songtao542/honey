package com.snt.phoney.ui.square.official

import android.R.attr.checked
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.widget.DropdownLabelView
import com.snt.phoney.widget.PopupList
import kotlinx.android.synthetic.main.fragment_official_recommend_list.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class OfficialRecommendFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_official_recommend_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = OfficialRecommendRecyclerViewAdapter()
        }
        list.setOnTouchListener { _, _ ->
            if (expandable.isExpanded && !expandable.isAnimating) {
                expandable.collapseWithAnimation()
            }
            false
        }
        publishTime.setOnClickListener {
            //expandFilter(publishTime, R.array.filter_publish_time)
            popupMenu(publishTime, R.array.filter_publish_time)
        }
        distance.setOnClickListener {
            //expandFilter(distance, R.array.filter_distance)
            popupMenu(distance, R.array.filter_distance)
        }
        datingContent.setOnClickListener {
            //expandFilter(datingContent, R.array.filter_dating_content)
            popupMenu(datingContent, R.array.filter_dating_content)
        }
    }

    private fun expandFilter(dropdownLabel: DropdownLabelView, menus: Int) {
        expandable.setIndicatorView(dropdownLabel.getIndicatorView())
        if (expandable.isExpanded) {
            expandable.collapseWithAnimation()
        } else {
            expandable.setFilter(context?.resources?.getStringArray(menus)?.asList())
        }
    }

    private fun popupMenu(dropdownLabel: DropdownLabelView, menus: Int) {
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
        }
        dropdownLabel.expandIndicator()
        list.show(anchor, Gravity.BOTTOM)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {

        @JvmStatic
        fun newInstance() = OfficialRecommendFragment()
    }
}
