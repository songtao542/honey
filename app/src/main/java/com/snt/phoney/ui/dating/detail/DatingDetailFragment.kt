package com.snt.phoney.ui.dating.detail

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders

import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment

class DatingDetailFragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = DatingDetailFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: DatingDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dating_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(DatingDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }


}
