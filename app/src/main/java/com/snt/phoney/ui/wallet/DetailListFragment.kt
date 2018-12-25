package com.snt.phoney.ui.wallet

import android.os.Bundle
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
import com.snt.phoney.utils.data.Constants
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.fragment_wallet_detail_list.*


/**
 *
 */
class DetailListFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = DetailListFragment().apply {
            this.arguments = arguments
        }

        @JvmStatic
        val TYPE_RECHARGE = 0
        @JvmStatic
        val TYPE_CONSUME = 1
    }

    private lateinit var viewModel: WalletViewModel
    private lateinit var adapter: DetailListRecyclerViewAdapter
    private var type: Int = TYPE_RECHARGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(Constants.Extra.TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet_detail_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WalletViewModel::class.java)

        list.layoutManager = LinearLayoutManager(requireContext())
        adapter = DetailListRecyclerViewAdapter(type)
        list.adapter = adapter

        if (type == TYPE_RECHARGE) {
            viewModel.rechargeOrders.observe(this, Observer {
                adapter.data = it
            })
        } else {
            viewModel.consumeOrders.observe(this, Observer {
                adapter.data = it
            })
        }

        list.setLoadMoreListener {
            load(false, it)
        }

        load(true)
    }


    private fun load(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            list.setLoadMoreEnable(true)
        }
        if (type == TYPE_RECHARGE) {
            viewModel.listRechargeOrder(refresh, loadMore = loadMore)
        } else {
            viewModel.listConsumeOrder(refresh, loadMore = loadMore)
        }
    }

}
