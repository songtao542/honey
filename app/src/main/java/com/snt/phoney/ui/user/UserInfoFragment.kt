package com.snt.phoney.ui.user

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.AlertDialogFragment
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.*
import com.snt.phoney.extensions.*
import com.snt.phoney.ui.album.AlbumActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.report.ReportActivity
import com.snt.phoney.ui.member.MemberActivity
import com.snt.phoney.ui.voicecall2.VoiceCallActivity
import com.snt.phoney.utils.Chat
import com.snt.phoney.utils.DistanceFormat
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoFlowAdapter
import kotlinx.android.synthetic.main.fragment_user_info1.*
import kotlinx.android.synthetic.main.fragment_user_info_header.*
import java.text.DecimalFormat
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.SpannableString

const val REQUEST_MEMBER_CODE = 60
const val REQUEST_VIEW_ALBUM_CODE = 62

class UserInfoFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = UserInfoFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: UserInfoViewModel

    private var user: User? = null

    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(Constants.Extra.UUID)
            user = it.getParcelable(Constants.Extra.USER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_user_info1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserInfoViewModel::class.java)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

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
            user?.let { u ->
                viewModel.follow(u.safeUuid)
            }
        }

        report.setOnClickListener {
            context?.let { ctx ->
                user?.let { u ->
                    ctx.startActivity<ReportActivity>(Page.REPORT, Bundle().apply {
                        putString(Constants.Extra.UUID, u.safeUuid)
                        putInt(Constants.Extra.TYPE, ReportType.USER.value)
                    })
                }
            }
        }

        chatButton.setOnClickListener {
            tryChat()
        }

        viewDating.setOnClickListener {
            user?.let { u ->
                context?.let { ctx ->
                    ctx.startActivity<DatingActivity>(Page.OTHERS_DATING, Bundle().apply {
                        putParcelable(Constants.Extra.USER, u)
                    })
                }
            }
        }

        voiceCallButton.setOnClickListener {
            context?.let { ctx ->
                user?.im?.let {
                    viewModel.getUser()?.let { me ->
                        val caller = JMUser.from(me)
                        val callee = JMUser.from(user)
                        VoiceCallActivity.start(ctx, caller, callee)
                        activity?.finish()
                        return@setOnClickListener
                    }
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
        val uid = uuid ?: user?.uuid
        uid?.let {
            viewModel.getUserInfo(it)
        }
    }

    private fun tryChat() {
        context?.let { ctx ->
            val isMember = viewModel.getUser()?.isValidMember ?: false
            if (isMember) {
                user?.let { u ->
                    if (u.isMember) {
                        u.im?.let { im ->
                            Chat.start(ctx, im)
                        }
                    }
                }
            } else {
                AlertDialogFragment.Builder(ctx)
                        .setTitle(R.string.notice)
                        .setMessage(R.string.chat_warning)
                        .setCancelable(false)
                        .setNegativeButton(R.string.cancel) { dialog ->
                            dialog.dismiss()
                        }.setPositiveButton(R.string.confirm) { dialog ->
                            dialog.dismiss()
                            startActivityForResult<MemberActivity>(Page.MEMBER, REQUEST_MEMBER_CODE)
                        }.show(childFragmentManager)
            }
            return@let
        }
    }

    private fun setupUserInfo(userInfo: User?) {
        userInfo?.let { user ->
            Glide.with(this).load(user.avatar)
                    .apply(RequestOptions().circleCrop().placeholder(R.drawable.ic_head_placeholder).error(R.drawable.ic_head_placeholder))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(head)
            titleTextView.text = user.nickname
            address.text = user.city
            userAge.text = getString(R.string.age_value_template, user.age)
            job.text = user.career
            if (user.verified) {
                authenticate.isSelected = true
                authenticate.text = getString(R.string.official_authenticated)
            } else {
                authenticate.isSelected = false
                authenticate.text = getString(R.string.not_authenticated)
            }
            distance.text = getString(R.string.distance_of_template, DistanceFormat.format(requireContext(), user.distance))

            program.text = user.program

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

            if (user.price <= 0) {
                voiceCallButton.setOnClickListener { snackbar(R.string.voice_not_available) }
                chatLimit.setText(R.string.voice_not_available)
            } else {
                chatLimit.text = getString(R.string.chat_price_template, df.format(user.price))
            }

            user.photos?.let { photos ->
                if (user.isPhotoNeedUnlock && viewModel.buySuccess.value != true) {
                    unlockPhotoLayout.visibility = View.VISIBLE
                    unlockPhotoLayout.setOnClickListener { }
                    needApplyOrCharge.setText(R.string.the_user_set_view_need_charge)
                    unlockOrApplyPhoto.text = getString(R.string.unlock_photo_template, user.photoPrice.toString())

                    unlockOrApplyPhoto.setOnClickListener {
                        buy(user.photoPrice) {
                            viewModel.buyWithMibi(OrderType.USE_UNLOCK_ALBUM_MIBI, user.photoId.toString(), user.uuid)
                        }
                    }
                } else if (user.isPhotoNeedApply) {
                    unlockPhotoLayout.visibility = View.VISIBLE
                    unlockPhotoLayout.setOnClickListener { }
                    @Suppress("CascadeIf")
                    if (user.photoApplyStatus == 2) {
                        needApplyOrCharge.setText(R.string.photo_apply_be_reject)
                        unlockOrApplyPhoto.setText(R.string.apply_view_photo_again)
                        unlockOrApplyPhoto.visibility = View.GONE
                    } else if (user.photoApplyStatus == 0) {
                        needApplyOrCharge.setText(R.string.photo_applying_on_going_tip)
                        unlockOrApplyPhoto.setText(R.string.photo_applying_on_going)
                        unlockOrApplyPhoto.visibility = View.GONE
                    } else {
                        needApplyOrCharge.setText(R.string.the_user_set_view_need_apply)
                        unlockOrApplyPhoto.visibility = View.VISIBLE
                        unlockOrApplyPhoto.setText(R.string.apply_view_photo)
                        unlockOrApplyPhoto.setOnClickListener {
                            viewModel.applyToViewPhotos(user.safeUuid)
                        }
                    }
                } else {
                    unlockPhotoLayout.visibility = View.GONE
                }

                photosView.viewAdapter = PhotoFlowAdapter(requireContext()).setViewerIsMember(user.isMember).setPhotos(photos).setMaxShow(20).setLastAsAdd(false)
                photosView.setOnItemClickListener { view, _ ->
                    val photo = view.getTag(R.id.tag) as? Photo
                    photo?.let { thePhoto ->
                        if (thePhoto.flag == 1 && !thePhoto.paid) {
                            buy(thePhoto.price) {
                                viewModel.buyWithMibi(OrderType.USE_RED_ENVELOPE_MIBI, thePhoto.id.toString(), user.uuid)
                            }
                        } else {
                            user.freePhotos?.let { freePhotos ->
                                val i = freePhotos.indexOf(thePhoto)
                                startActivityForResult<AlbumActivity>(Page.ALBUM_VIEWER, REQUEST_VIEW_ALBUM_CODE, Bundle().apply {
                                    putParcelableArrayList(Constants.Extra.PHOTO_LIST, ArrayList(freePhotos))
                                    putInt(Constants.Extra.INDEX, i)
                                    putBoolean(Constants.Extra.IS_MEMBER, user.isMember)
                                    putBoolean(Constants.Extra.DELETABLE, false)
                                })
                            }
                        }
                        return@let
                    }
                }
            }

            if (user.isCared) {
                follow.setImageResource(R.drawable.ic_heart_solid_red)
            } else {
                follow.setImageResource(R.drawable.ic_heart_solid)
            }

            introduce.text = user.introduce
            frequentCity.text = user.cities?.map { it.name }?.joinToString(separator = ",") ?: ""

            //TODO 服务器返回值，字段名错误，后期提醒修改
            //setProgram(user.program)

            if (user.hasWechatAccount && TextUtils.isEmpty(user.wechatAccount)) {
                wechatAccount.isSelected = true
                wechatAccount.setText(R.string.click_to_view_wechat_account)
                if (user.isMember) {
                    wechatAccount.setOnClickListener { viewModel.getUserWechatAccount(user.safeUuid) }
                } else {
                    wechatAccount.setOnClickListener { startActivityForResult<MemberActivity>(Page.MEMBER, REQUEST_MEMBER_CODE) }
                }
            } else if (!TextUtils.isEmpty(user.wechatAccount)) {
                wechatAccount.text = user.wechatAccount
            } else {
                wechatAccount.isSelected = false
                wechatAccount.setText(R.string.no_wechat_account)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEMBER_CODE && resultCode == Activity.RESULT_OK) {
            loadUser()
        }
        if (requestCode == REQUEST_VIEW_ALBUM_CODE && resultCode == Activity.RESULT_OK) {
            val isMember = data?.getBooleanExtra(Constants.Extra.IS_MEMBER, false) ?: false
            if (isMember) {
                loadUser()
            } else {
                val photos = data?.getParcelableArrayListExtra<Photo>(Constants.Extra.LIST)
                photos?.let { updatePhoto(it) }
            }
        }
    }

    private fun updatePhoto(photos: List<Photo>) {
        user?.photos?.let { ups ->
            for (photo in photos) {
                for (p in ups) {
                    if (p.id == photo.id) {
                        p.burn = photo.burn
                    }
                }
            }
            photosView.notifyAdapterSizeChanged()
        }
    }

    private fun buy(price: Int, handler: (() -> Unit)) {
        context?.let { ctx ->
            val message = getString(R.string.buy_warning_template, price.toString())
            val spannableString = SpannableString(message)
            val colorSpan = ForegroundColorSpan(Color.RED)
            spannableString.setSpan(colorSpan, message.indexOf(price.toString()), spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            AlertDialogFragment.Builder(ctx)
                    .setTitle(R.string.buy_tip)
                    .setMessage(spannableString)
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel) { dialog ->
                        dialog.dismiss()
                    }.setPositiveButton(R.string.confirm) { dialog ->
                        dialog.dismiss()
                        handler.invoke()
                    }.show(childFragmentManager)
            return@let
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
            user?.uuid?.let {
                viewModel.getUserInfo(it)
            }
        }
    }

}
