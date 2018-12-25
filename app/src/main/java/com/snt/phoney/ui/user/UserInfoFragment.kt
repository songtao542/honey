package com.snt.phoney.ui.user

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.*
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.report.ReportActivity
import com.snt.phoney.ui.voicecall.VoiceCallActivity
import com.snt.phoney.utils.Chat
import com.snt.phoney.utils.DistanceFormat
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoFlowAdapter
import kotlinx.android.synthetic.main.fragment_user_info.*
import kotlinx.android.synthetic.main.fragment_user_info_header.*
import java.text.DecimalFormat

class UserInfoFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = UserInfoFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: UserInfoViewModel

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(Constants.Extra.USER)
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

        setupUserInfo(user)

        viewModel.userInfo.observe(this, Observer {
            it?.let { u ->
                user = u
                setupUserInfo(u)
            }
        })

        viewModel.error.observe(this, Observer {
            it?.let { message ->
                snackbar(message)
                viewModel.error.value = null
            }
        })

        viewModel.success.observe(this, Observer {
            it?.let { message ->
                snackbar(message)
                viewModel.success.value = null
            }
        })

        viewModel.followSuccess.observe(this, Observer {
            it?.let { success ->
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

        viewModel.buySuccess.observe(this, Observer {
            loadUser()
        })

        follow.setOnClickListener {
            viewModel.follow(user.safeUuid)
        }

        report.setOnClickListener {
            context?.let { context ->
                context.startActivity(CommonActivity.newIntent<ReportActivity>(context, Page.CREATE_REPORT, Bundle().apply {
                    putString(Constants.Extra.UUID, user.safeUuid)
                    putInt(Constants.Extra.TYPE, ReportType.USER.value)
                }))
            }
        }

        chatButton.setOnClickListener {
            context?.let { context ->
                user.im?.let { im ->
                    Chat.start(context, im)
                }
            }
        }

        viewDating.setOnClickListener {
            user?.let { user ->
                context?.let { context ->
                    context.startActivity(CommonActivity.newIntent<DatingActivity>(context, Page.VIEW_OTHERS_DATING, Bundle().apply {
                        putParcelable(Constants.Extra.USER, user)
                    }))
                }
            }
        }

        chatWith.setOnClickListener {
            user?.let {
                context?.let { context ->
                    context.startActivity(Intent(context, VoiceCallActivity::class.java))
                    activity?.finish()
                    return@setOnClickListener
                }
            }
        }

        if (checkPermission()) {
            loadUser()
        } else {
            checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        }

    }

    private fun loadUser() {
        user.uuid?.let {
            viewModel.getUserInfo(it)
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
        if (user.verified) {
            authenticate.text = getString(R.string.official_authenticated)
        } else {
            authenticate.text = getString(R.string.not_authenticated)
        }
        distance.text = getString(R.string.distance_of_template, DistanceFormat.format(requireContext(), user.distance))

        height.text = "${user.height}"
        age.text = "${user.age}"

        val df = DecimalFormat.getInstance()

        if (user.sex == Sex.FEMALE.value) {
            cupWeightLabel.text = getString(R.string.cup_label)
            cupWeight.text = user.cup
        } else {
            cupWeightLabel.text = getString(R.string.weight_label)
            cupWeight.text = "${df.format(user.weight)}"
        }

        chatLimit.text = getString(R.string.chat_price_template, df.format(user.price))

        user.photos?.let {
            photos.viewAdapter = PhotoFlowAdapter(requireContext()).setPhotos(it).setMaxShow(12).setLastAsAdd(false)
            photos.setOnItemClickListener { view, _ ->
                val photo = view.getTag(R.id.tag) as? Photo
                photo?.let { photo ->
                    if (TextUtils.isEmpty(photo.path)) {
                        buy(photo)
                    }
                }
            }
        }

        if (user.care) {
            follow.setImageResource(R.drawable.ic_heart_solid_red)
        } else {
            follow.setImageResource(R.drawable.ic_heart_solid)
        }

        introduce.text = user.introduce
        frequentCity.text = user.cities?.map { it.name }?.joinToString(separator = ",") ?: ""
        //TODO 服务器返回值，字段名错误，后期提醒修改
        setProgram(user.program)

    }

    private fun buy(photo: Photo) {
        context?.let { context ->
            AlertDialog.Builder(context)
                    .setTitle(R.string.buy_tip)
                    .setMessage(R.string.buy_warning)
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }.setPositiveButton(R.string.confirm) { dialog, _ ->
                        dialog.dismiss()
                        viewModel.buyWithMibi(OrderType.USE_PHOTO_MIBI, photo.id.toString(), user.uuid)
                    }.show()
        }
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
            user.uuid?.let {
                viewModel.getUserInfo(it)
            }
        }
    }

}
