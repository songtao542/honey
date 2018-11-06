package com.snt.phoney.ui.password

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.ForgetPasswordFragmentBinding
import com.snt.phoney.extensions.autoCleared
import kotlinx.android.synthetic.main.fragment_forget_password.*

/**
 *
 */
class ForgetPasswordFragment : BaseFragment() {

    var binding by autoCleared<ForgetPasswordFragmentBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forget_password, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener {
            activity?.let { it.supportFragmentManager.popBackStack() }
        }

        uploadConfirm.setOnClickListener {
            uploadLayout.visibility = View.GONE
            successLayout.visibility = View.VISIBLE
        }

        backHome.setOnClickListener {
            activity?.let { it.supportFragmentManager.popBackStack() }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }


    companion object {
        @JvmStatic
        fun newInstance() = ForgetPasswordFragment()
    }
}
