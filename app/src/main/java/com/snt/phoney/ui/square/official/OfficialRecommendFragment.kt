package com.snt.phoney.ui.square.official

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.MenuPopupWindow
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_official_recommend_list.*
import kotlinx.android.synthetic.main.fragment_official_recommend_list.view.*

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

//        ArrayAdapter.createFromResource(
//                context!!,
//                R.array.filter_publish_time,
//                android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }

        publishTime.setOnClickListener {
            if (expandable.isExpanded) {
                expandable.toggle()
            } else {
                expandable.setFilter(context?.resources?.getStringArray(R.array.filter_publish_time)?.asList())
                expandable.toggle()
            }
        }
        distance.setOnClickListener {
            context?.let { context ->
                val menu = PopupMenu(context, anchor, Gravity.CENTER, 0, R.style.PopupMenu)
                menu.inflate(R.menu.navigation)

                menu.show()
            }

        }
        datingContent.setOnClickListener {

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
        fun newInstance() = OfficialRecommendFragment()
    }
}
