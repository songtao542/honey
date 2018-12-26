package com.snt.phoney.ui.dating.detail

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.domain.model.DatingState
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoFlowAdapter
import kotlinx.android.synthetic.main.fragment_dating_detail.*
import kotlinx.android.synthetic.main.fragment_dating_detail_header.*
import kotlinx.android.synthetic.main.fragment_dating_detail_join_success.*
import java.text.DecimalFormat

class DatingDetailFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = DatingDetailFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: DatingDetailViewModel
    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(Constants.Extra.UUID, null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dating_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DatingDetailViewModel::class.java)

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        viewModel.dating.observe(this, Observer {
            setDating(it)
        })

        viewModel.joinSuccess.observe(this, Observer {
            detailLayout.visibility = View.GONE
            joinSuccessStub.inflate()
            successToolbar.setNavigationOnClickListener { activity?.onBackPressed() }
            joinSuccessButton.setOnClickListener { activity?.onBackPressed() }
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        joinButton.setOnClickListener {
            uuid?.let {
                viewModel.joinDating(it)
            }
        }

        if (checkPermission()) {
            uuid?.let {
                viewModel.getDatingDetail(it)
            }
        } else {
            checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun checkPermission(): Boolean {
        return context?.checkAppPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) == true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (checkPermission()) {
            uuid?.let {
                viewModel.getDatingDetail(it)
            }
        }
    }

    private fun setUser(user: User) {
        toolbarTitle.text = user.nickname

        Glide.with(this).load(user.portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
        val df = DecimalFormat.getInstance()
        //address.text = user.city
        userAge.text = getString(R.string.age_value_template, user.age)
        job.text = user.career
        //distance.text = getString(R.string.distance_of_template, " ${df.format(user.distance)}")

        height.text = "${user.height}"
        age.text = "${user.age}"

        if (user.verified == 1) {
            authenticate.text = getString(R.string.official_authenticated)
        } else {
            authenticate.text = getString(R.string.not_authenticated)
        }

        if (user.sex == Sex.FEMALE.value) {
            cupWeightLabel.text = getString(R.string.cup_label)
            cupWeight.text = user.cup
        } else {
            cupWeightLabel.text = getString(R.string.weight_label)
            cupWeight.text = "${df.format(user.weight)}"
        }
    }

    private fun setDating(dating: Dating) {
        dating.user?.let {
            setUser(it)
        }

        photos.viewAdapter = PhotoFlowAdapter(requireContext()).setUrls(dating.cover?.map {
            it.path ?: ""
        } ?: emptyList()).setMaxShow(12).setLastAsAdd(false)

        address.text = dating.city
        val df = DecimalFormat.getInstance()
        distance.text = getString(R.string.distance_of_template, "${df.format(dating.distance)}m")
        datingAddress.text = dating.location
        datingTime.text = dating.formatTime()

        if (dating.state == DatingState.ONGOING.value || dating.remaining() > 0) {
            remainingTime.text = dating.remainingTime()
        } else {
            when {
                dating.state == DatingState.EXPIRED.value -> remainingTime.setText(R.string.has_out_of_time)
                dating.state == DatingState.FINISHED.value -> remainingTime.setText(R.string.has_finish)
                dating.state == DatingState.CANCELED.value -> remainingTime.setText(R.string.has_canceled)
            }
        }

    }

}
