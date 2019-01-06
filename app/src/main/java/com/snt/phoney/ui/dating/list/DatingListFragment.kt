package com.snt.phoney.ui.dating.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.setLoadMoreEnable
import com.snt.phoney.extensions.setLoadMoreListener
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.utils.data.Constants
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_dating_list.*

class DatingListFragment : BaseFragment() {

    companion object {
        /**
         * 我发布的
         */
        val TYPE_PUBLISH = 0
        /**
         * 我参加的
         */
        val TYPE_JOINED = 1
        /**
         * 别人发布的
         */
        val TYPE_OTHER = 1

        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = DatingListFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: DatingViewModel

    private lateinit var adapter: DatingListRecyclerViewAdapter

    private var user: User? = null
    private var type: Int = TYPE_PUBLISH
    private var toolbarVisibility = View.VISIBLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(Constants.Extra.USER)
            type = it.getInt(Constants.Extra.TYPE, 0)
            toolbarVisibility = it.getInt("TOOLBAR_VISIBILITY", View.VISIBLE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_dating_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DatingViewModel::class.java)

        list.layoutManager = LinearLayoutManager(context)
        adapter = DatingListRecyclerViewAdapter(this, viewModel, type)
        list.adapter = adapter

        toolbar.visibility = toolbarVisibility
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.dating_list_title)

        viewModel.error.observe(this, Observer {
            it?.let { message ->
                snackbar(message)
                viewModel.error.value = null
            }
        })

        when (type) {
            TYPE_PUBLISH -> viewModel.publishDatings.observe(this, Observer {
                adapter.data = it
            })
            TYPE_JOINED -> {
                viewModel.joinedDatings.observe(this, Observer {
                    adapter.data = it
                })
                viewModel.quitSuccess.observe(this, Observer {
                    it?.let { message ->
                        snackbar(message)
                        adapter.notifyDataSetChanged()
                        viewModel.quitSuccess.value = null
                    }
                })
            }
            TYPE_OTHER -> viewModel.otherDatings.observe(this, Observer {
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
        if (type == TYPE_PUBLISH) {
            viewModel.listMyDating(refresh, loadMore)
        } else if (type == TYPE_JOINED) {
            viewModel.listJoinedDating(refresh, loadMore)
        } else if (type == TYPE_OTHER) {
            if (user != null) {
                viewModel.listDating(refresh, user!!.safeUuid, loadMore)
            }
        }
    }

}
