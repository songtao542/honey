package com.snt.phoney.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_bind_phone.*

class BindPhoneFragment : BaseDialogFragment() {

    companion object {
        fun newInstance() = BindPhoneFragment()
    }

    private lateinit var viewModel: BindPhoneViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bind_phone, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BindPhoneViewModel::class.java)

        getVerificationCode.setOnClickListener {
            viewModel.requestVerificationCode(phone.text.toString())
        }
        bindPhone.setOnClickListener {
            viewModel.bindPhone(phone.text.toString(), verificationCode.text.toString())
        }
        cancel.setOnClickListener { dismiss() }

        viewModel.error.observe(this, Observer { message ->
            context?.let { context ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        })
        viewModel.verificationCodeId.observe(this, Observer { message ->
            context?.let { context ->
                Toast.makeText(context, getString(R.string.verification_code_has_send), Toast.LENGTH_LONG).show()
            }
        })
        viewModel.success.observe(this, Observer { message ->
            context?.let { context ->
                Toast.makeText(context, getString(R.string.bind_phone_success), Toast.LENGTH_LONG).show()
                dismiss()
            }
        })
    }

}
