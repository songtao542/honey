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
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.*
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.photo.PhotoViewerActivity
import com.snt.phoney.ui.report.ReportActivity
import com.snt.phoney.utils.DistanceFormat
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

        viewModel.followSuccess.observe(this, Observer { isFollowed ->
            isFollowed?.let { success ->
                if (success) {
                    snackbar(getString(R.string.has_follow))
                    follow.setImageResource(R.drawable.ic_heart_solid_red)
                } else {
                    snackbar(getString(R.string.has_canceld_follow))
                    follow.setImageResource(R.drawable.ic_heart_solid)
                }
                viewModel.followSuccess.value = null
            }
        })

        joinButton.setOnClickListener {
            uuid?.let { uuid ->
                viewModel.joinDating(uuid)
            }
        }

        follow.setOnClickListener {
            viewModel.dating.value?.user?.uuid?.let { uuid ->
                viewModel.follow(uuid)
            }
        }

        report.setOnClickListener {
            context?.let { context ->
                uuid?.let { uuid ->
                    context.startActivity<ReportActivity>(Page.REPORT, Bundle().apply {
                        putString(Constants.Extra.UUID, uuid)
                        putInt(Constants.Extra.TYPE, ReportType.DATING.value)
                    })
                }
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

        Glide.with(this).load(user.avatar).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
        val df = DecimalFormat.getInstance()
        //address.text = user.city
        userAge.text = getString(R.string.age_value_template, user.age)
        job.text = user.career

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

        photosView.viewAdapter = PhotoFlowAdapter(requireContext()).setUrls(dating.cover?.map {
            it.path ?: ""
        } ?: emptyList()).setMaxShow(12).setLastAsAdd(false)

        photosView.setOnItemClickListener { _, index ->
            viewModel.dating.value?.let { dating ->
                startActivity<PhotoViewerActivity>(Page.PHOTO_VIEWER, Bundle().apply {
                    putStringArrayList(Constants.Extra.URL_LIST, ArrayList(dating.photoUrls()))
                    putInt(Constants.Extra.INDEX, index)
                    putBoolean(Constants.Extra.DELETABLE, false)
                })
            }
        }

        address.text = dating.city

        context?.let { context ->
            distance.text = getString(R.string.distance_of_template, DistanceFormat.format(context, dating.distance))
        }
        datingAddress.text = dating.location
        datingTime.text = dating.formatTime()

        if (dating.state == DatingState.ONGOING.value || dating.remaining() > 0) {
            remainingTime.text = dating.remainingTime()
        } else if (dating.remaining() <= 0) {
            remainingTime.setText(R.string.has_out_of_time)
        } else {
            when {
                dating.state == DatingState.EXPIRED.value -> remainingTime.setText(R.string.has_out_of_time)
                dating.state == DatingState.FINISHED.value -> remainingTime.setText(R.string.has_finish)
                dating.state == DatingState.CANCELED.value -> remainingTime.setText(R.string.has_canceled)
            }
        }

        if (dating.isCared) {
            follow.setImageResource(R.drawable.ic_heart_solid_red)
        } else {
            follow.setImageResource(R.drawable.ic_heart_solid)
        }

        if (dating.isAttend) {
            joinButton.visibility = View.GONE
        } else {
            joinButton.visibility = View.VISIBLE
        }

    }

}
