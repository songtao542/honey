package com.snt.phoney.ui.home.following

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment

import com.snt.phoney.ui.home.following.dummy.DummyContent
import com.snt.phoney.ui.home.following.dummy.DummyContent.DummyItem
import com.snt.phoney.ui.home.friend.FriendFragment

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FollowingFragment.OnListFragmentInteractionListener] interface.
 */
class FollowingFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_following_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = FollowingRecyclerViewAdapter(DummyContent.ITEMS)
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
        fun newInstance() = FollowingFragment()
    }
}
