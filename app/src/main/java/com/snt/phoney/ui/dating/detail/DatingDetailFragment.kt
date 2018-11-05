package com.snt.phoney.ui.dating.detail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import com.snt.phoney.R
import com.snt.phoney.base.Page
import com.snt.phoney.ui.dating.DatingActivity

class DatingDetailFragment : Fragment() {

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
        viewModel = ViewModelProviders.of(this).get(DatingDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }


}
