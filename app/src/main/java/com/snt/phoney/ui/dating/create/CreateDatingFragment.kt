package com.snt.phoney.ui.dating.create

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.CreateDatingFragmentBinding
import com.snt.phoney.extensions.autoCleared
import kotlinx.android.synthetic.main.fragment_dating_create.*

class CreateDatingFragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = CreateDatingFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: CreateDatingViewModel

    var binding by autoCleared<CreateDatingFragmentBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dating_create, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(toolbar)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateDatingViewModel::class.java)
        binding.toolbar.setNavigationOnClickListener { activity?.finish() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.create_dating, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.publishDatingConfirm -> {
                Log.d("TTTT", "Confirm publish")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
