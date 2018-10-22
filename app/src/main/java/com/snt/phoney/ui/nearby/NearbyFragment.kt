package com.snt.phoney.ui.nearby

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.NearbyFragmentBinding
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.ui.nearby.dummy.DummyContent
import kotlinx.android.synthetic.main.fragment_nearby_list.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class NearbyFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var viewModel: NearbyViewModel

    var binding by autoCleared<NearbyFragmentBinding>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nearby_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(list) {
            layoutManager = GridLayoutManager(context, 3)
            adapter = NearbyRecyclerViewAdapter(DummyContent.ITEMS)
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NearbyViewModel::class.java)

        binding.viewModel = viewModel
        binding.toolbar.setNavigationOnClickListener { activity?.finish() }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }


    companion object {
        @JvmStatic
        fun newInstance() = NearbyFragment()
    }
}
