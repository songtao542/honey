package com.snt.phoney.ui.mine

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import kotlinx.android.synthetic.main.fragment_mine_header.*
import kotlinx.android.synthetic.main.fragment_mine_list.*

/**
 * A fragment representing a list of Items.
 */
class MineFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(mineRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = MineRecyclerViewAdapter(this@MineFragment)
        }

        editInfo.setOnClickListener { context?.let { context -> startActivity(CommonActivity.newIntent(context, Page.EDIT_USER_INFO)) } }
        upgradeVip.setOnClickListener { context?.let { context -> startActivity(CommonActivity.newIntent(context, Page.UPGRADE_VIP)) } }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}
