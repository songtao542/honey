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
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_othersdating_list.*

/**
 * A fragment representing a list of Items.
 */
class OthersDatingFragment : BaseFragment() {

    private lateinit var viewModel: DatingViewModel

    private lateinit var adapter: OthersDatingRecyclerViewAdapter

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(Constants.Extra.USER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_othersdating_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DatingViewModel::class.java)

        list.layoutManager = LinearLayoutManager(context)
        adapter = OthersDatingRecyclerViewAdapter(this, viewModel)
        list.adapter = adapter

        viewModel.otherDatings.observe(this, Observer {
            adapter.data = it
        })
        viewModel.followSuccess.observe(this, Observer {
            it?.let { follow ->
                if (follow) {
                    snackbar(R.string.has_follow)
                } else {
                    snackbar(R.string.has_canceld_follow)
                }
                adapter.notifyDataSetChanged()
                viewModel.followSuccess.value = null
            }
        })

        user?.let {
            viewModel.listDating(it.safeUuid)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = OthersDatingFragment().apply {
            this.arguments = arguments
        }
    }
}
