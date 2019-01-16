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
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.extensions.loadMore
import com.snt.phoney.extensions.setLoadMoreEnable
import com.snt.phoney.extensions.setLoadMoreListener
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.utils.data.Constants
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_applicant_list.*


/**
 *
 */
class ApplicantListFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(argument: Bundle?) = ApplicantListFragment().apply {
            arguments = argument
        }
    }

    private var dating: Dating? = null
    private var uuid: String? = null

    private lateinit var viewModel: DatingViewModel
    private lateinit var adapter: ApplicantListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(Constants.Extra.UUID)
            dating = it.getParcelable(Constants.Extra.DATA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_applicant_list, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DatingViewModel::class.java)

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.dating_applying_title)

        list.layoutManager = LinearLayoutManager(requireContext())
        adapter = ApplicantListRecyclerViewAdapter(viewModel)
        list.adapter = adapter

        viewModel.applicants.observe(this, Observer {
            adapter.data = it
        })

        viewModel.error.observe(this, Observer {
            it?.let { message ->
                snackbar(message)
                viewModel.error.value = null
            }
        })

        viewModel.reviewSuccess.observe(this, Observer {
            it?.let { message ->
                adapter.notifyDataSetChanged()
                snackbar(message)
                viewModel.reviewSuccess.value = null
            }
        })

        list.setLoadMoreListener {
            load(false, it)
        }

        load(true, list.loadMore)
    }

    private fun load(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            list.setLoadMoreEnable(true)
        }
        val id = uuid ?: dating?.uuid
        if (id != null) {
            viewModel.listDatingApplicant(refresh, id, loadMore)
        } else {
            viewModel.listApplicant(refresh, loadMore)
        }
    }

}
