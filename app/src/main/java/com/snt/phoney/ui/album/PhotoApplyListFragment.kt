package com.snt.phoney.ui.album

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
import com.snt.phoney.extensions.snackbar
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_album_photo_apply_list.*


/**
 *
 */
class PhotoApplyListFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(argument: Bundle?) = PhotoApplyListFragment().apply {
            arguments = argument
        }
    }

    private lateinit var viewModel: AlbumViewModel
    private lateinit var adapter: PhotoApplyListRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_album_photo_apply_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlbumViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.photo_applying_title)

        list.layoutManager = LinearLayoutManager(requireContext())
        adapter = PhotoApplyListRecyclerViewAdapter(this, viewModel)
        list.adapter = adapter

        viewModel.photoApplyList.observe(this, Observer {
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

        load(true)
    }

    private fun load(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            list.setLoadMoreEnable(true)
        }
        viewModel.listPhotoApply(refresh, loadMore)
    }

}
