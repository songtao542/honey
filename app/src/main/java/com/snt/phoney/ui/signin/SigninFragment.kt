package com.snt.phoney.ui.signin

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.SigninFragmentBinding

import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.extensions.disposedBy

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SigninFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SigninFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SigninFragment : BaseFragment() {

    lateinit var viewModel: SigninViewModel
    var binding by autoCleared<SigninFragmentBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SigninViewModel::class.java)
        binding.loginViewModel = viewModel

        binding.login.setOnClickListener { onLoginButtonPressed() }

        binding.test.setOnClickListener { v ->
            var user: User? = viewModel.user
            Log.d("TTTT", "onActivityCreated, user:$user")
        }


    }

    fun onLoginButtonPressed() {
        Log.d("TTTT", "click login " + viewModel)
        viewModel.signin("songtao", "wangsongtao").subscribe { response: Response<User>? ->
            Log.d("TTTT", "response:" + response?.data)
            viewModel.updateUser(response?.data)
        }.disposedBy(disposeBag)

//        viewModel.login("songtao", "wangsongtao").observe(this, Observer {
//            Log.d("TTTT", "data:" + it?.data)
//        })


    }

    companion object {
        @JvmStatic
        fun newInstance() = SigninFragment()
    }
}
