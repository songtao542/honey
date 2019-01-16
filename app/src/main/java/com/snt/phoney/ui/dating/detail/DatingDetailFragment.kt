package com.snt.phoney.ui.dating.detail

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
import kotlinx.android.synthetic.main.fragment_dating_detail1.*
import kotlinx.android.synthetic.main.fragment_dating_detail_header.*
import kotlinx.android.synthetic.main.fragment_dating_detail_join_success.*
import java.text.DecimalFormat
import java.util.*

class DatingDetailFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = DatingDetailFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: DatingDetailViewModel
    private var uuid: String? = null

    private var mHandler = Handler()
    private var df = DecimalFormat.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(Constants.Extra.UUID, null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dating_detail1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DatingDetailViewModel::class.java)

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        nestedLayout.setAutoScroll(false)
        nestedLayout.setCalculateMaxScrollHeight(true)
        toolbar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                toolbar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                nestedLayout.setMinHeight(toolbar.height)
            }
        })
        nestedLayout.setOnTopVisibleHeightChangeListener { heightEnough, totalHeight, visibleHeight ->
            if (heightEnough) {
                headerLayout.alpha = 1f - (visibleHeight.toFloat() / totalHeight.toFloat())
            } else {
                headerLayout.alpha = 1f - (visibleHeight.toFloat() / totalHeight.toFloat()) + 0.2f
            }
        }

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
        } else {
            activity?.onBackPressed()
        }
    }

    private fun setUser(user: User) {
        titleTextView.text = user.nickname

        Glide.with(this).load(user.avatar)
                .apply(RequestOptions().circleCrop().placeholder(R.drawable.ic_head_placeholder).error(R.drawable.ic_head_placeholder))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(head)
        val df = DecimalFormat.getInstance()
        //address.text = user.city
        userAge.text = getString(R.string.age_value_template, user.age)
        job.text = user.career

        height.text = "${user.height}"
        age.text = "${user.age}"

        if (user.verified) {
            authenticate.isSelected = true
            authenticate.text = getString(R.string.official_authenticated)
        } else {
            authenticate.isSelected = false
            authenticate.text = getString(R.string.not_authenticated)
        }

        if (user.sex == Sex.FEMALE.value) {
            cupWeightLabel.text = getString(R.string.cup_label)
            cupWeight.text = user.cup
        } else {
            cupWeightLabel.text = getString(R.string.weight_label)
            cupWeight.text = "${df.format(user.weight)}"
        }

        if (user.uuid == viewModel.getCurrentUserId()) {
            joinButton.visibility = View.GONE
        } else {
            joinButton.visibility = View.VISIBLE
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
            remainingCountDown(dating.endTime)
        } else if (dating.remaining() <= 0) {
            remainingTime.setText(R.string.has_out_of_time)
            joinButton.isEnabled = false
        } else {
            joinButton.isEnabled = false
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
        }

        program.text = dating.program
    }

    //private var calendar: Calendar = Calendar.getInstance()
    private var days: Long = -1
    private var hours: Long = -1
    private var minutes: Long = -1
    private var second: Long = -1

    @Suppress("CascadeIf")
    private fun remainingCountDown(time: Long) {
        if (time == null) {
            return
        }
        val diff = time - System.currentTimeMillis()
        if (diff <= 0) {
            joinButton.isEnabled = false
            remainingTime.setText(R.string.has_out_of_time)
        }
        //calendar.timeInMillis = time

        if (second == -1L) {
            second = diff / 1000
            days = second / 86400            //转换天数
            second %= 86400            //剩余秒数
            hours = second / 3600            //转换小时
            second %= 3600                //剩余秒数
            minutes = second / 60            //转换分钟
            second %= 60                //剩余秒数
        } else {
            second -= 1
            if (second == 0L) {
                minutes -= 1
                if (minutes == 0L) {
                    hours -= 1
                    if (hours == 0L) {
                        days -= 1
                        if (days == -1L) {
                            days = 0
                        }
                    }
                    if (hours == -1L) {
                        hours = 23
                    }
                }
                if (minutes == -1L) {
                    minutes = 59
                }
            }
            if (second == -1L) {
                second = 59
            }
        }
        if (days > 0) {
            remainingTime.text = getString(R.string.day_hour_minute_second_template, df.format(days), df.format(hours), df.format(minutes), df.format(second))
        } else if (hours > 0) {
            remainingTime.text = getString(R.string.hour_minute_second_template, df.format(hours), df.format(minutes), df.format(second))
        } else {
            remainingTime.text = getString(R.string.minute_second_template, df.format(minutes), df.format(second))
        }
        mHandler.postDelayed({
            remainingCountDown(time - 1000)
        }, 1000)
    }

    override fun onDestroyView() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

}
