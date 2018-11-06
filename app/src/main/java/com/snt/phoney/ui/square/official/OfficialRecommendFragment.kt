package com.snt.phoney.ui.square.official

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.ui.square.official.dummy.DummyContent
import com.snt.phoney.ui.square.official.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class OfficialRecommendFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_official_recommend_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                //layoutManager = GridLayoutManager(context, 3)
                layoutManager = LinearLayoutManager(context)
                adapter = OfficialRecommendRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view
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
