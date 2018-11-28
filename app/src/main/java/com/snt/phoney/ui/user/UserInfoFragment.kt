package com.snt.phoney.ui.user

import android.Manifest
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.dip
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoWallFactory
import kotlinx.android.synthetic.main.fragment_user_info.*
import kotlinx.android.synthetic.main.fragment_user_info_header.*

class UserInfoFragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = UserInfoFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: UserInfoViewModel

    private lateinit var argUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argUser = it.getParcelable(Constants.Extra.USER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserInfoViewModel::class.java)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        setupUserInfo(argUser)

        viewModel.userInfo.observe(this, Observer {
            setupUserInfo(it)
        })


        if (checkPermission()) {
            argUser.uuid?.let {
                viewModel.getUserInfo(it)
            }
        } else {
            checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        }

    }

    private fun setupUserInfo(user: User?) {
        if (user == null) {
            return
        }
        Glide.with(this).load(user.portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
        address.text = user.city
        userAge.text = getString(R.string.age_value_template, user.age)
        job.text = user.career
        authenticate.text = user.career
        distance.text = getString(R.string.distance_of_template, " ${user.distance}")

        height.text = "${user.height}"
        age.text = "${user.age}"

        if (user.sex == Sex.FEMALE.value) {
            cupWeightLabel.text = getString(R.string.cup_label)
            cupWeight.text = user.cup
        } else {
            cupWeightLabel.text = getString(R.string.weight_label)
            cupWeight.text = "${user.weight}"
        }

        chatLimit.text = getString(R.string.chat_limit1)
        chatWith.setOnClickListener {

        }
        photos.viewFactory = PhotoWallFactory(requireContext()).setUrls(user.photos?.map {
            it.path ?: ""
        } ?: emptyList()).setMaxShow(12).setLastAsAdd(false)

        viewDating.setOnClickListener {

        }

        if (user.care) {
            follow.setImageResource(R.drawable.ic_heart_solid_red)
        } else {
            follow.setImageResource(R.drawable.ic_heart_solid)
        }

        introduce.text = user.introduce
        frequentCity.text = user.cities?.map { it.name }?.joinToString(separator = ",") ?: ""
        setProgram(user.program)
    }

    private fun setProgram(program: String?) {
        if (program == null) {
            return
        }
        val programs = program.split(",")
        if (programs.isEmpty()) {
            return
        }
        programContainer.removeAllViews()
        for (p in programs) {
            val tag = TextView(requireContext())
            tag.setBackgroundResource(R.drawable.user_info_plan_bg)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.leftMargin = dip(10)
            tag.layoutParams = lp
            tag.setPadding(dip(10), dip(6), dip(10), dip(6))
            tag.setTextSize(TypedValue.COMPLEX_UNIT_PX, requireContext().resources.getDimensionPixelSize(R.dimen.textsize_small).toFloat())
            programContainer.addView(tag)

            tag.text = p
        }
    }

    private fun checkPermission(): Boolean {
        return context?.checkAppPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) == true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (checkPermission()) {
            argUser.uuid?.let {
                viewModel.getUserInfo(it)
            }
        }
    }

}
