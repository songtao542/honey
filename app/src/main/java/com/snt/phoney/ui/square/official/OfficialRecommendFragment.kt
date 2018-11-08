package com.snt.phoney.ui.square.official

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.MenuPopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
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

        publishTime.setOnClickListener {
        }
        distance.setOnClickListener {

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
