package com.snt.phoney.ui.main.mine

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.User
import com.snt.phoney.ui.about.AboutActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.privacy.PrivacyActivity
import com.snt.phoney.ui.report.ReportActivity
import com.snt.phoney.ui.setup.BindPhoneFragment
import com.snt.phoney.ui.share.ShareFragment
import com.snt.phoney.ui.signup.SignupActivity
import com.snt.phoney.ui.user.UserActivity
import com.snt.phoney.ui.vip.VipActivity
import com.snt.phoney.ui.wallet.WalletActivity
import kotlinx.android.synthetic.main.fragment_mine_header.*
import kotlinx.android.synthetic.main.fragment_mine_list.*

/**
 * A fragment representing a list of Items.
 */
class MineFragment : BaseFragment(), OnSettingItemClickListener, OnSignOutClickListener {


    lateinit var viewModel: MineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var adapter: MineRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MineViewModel::class.java)

        list.layoutManager = LinearLayoutManager(context)
        adapter = MineRecyclerViewAdapter(this@MineFragment)
        adapter.setOnSettingItemClickListener(this)
        adapter.setOnSignOutClickListener(this)
        list.adapter = adapter

        editInfo.setOnClickListener { context?.let { context -> startActivity(CommonActivity.newIntent<UserActivity>(context, Page.EDIT_USER_INFO)) } }
        upgradeVip.setOnClickListener { context?.let { context -> startActivity(CommonActivity.newIntent<VipActivity>(context, Page.UPGRADE_VIP)) } }

        viewModel.amountInfo.observe(this, Observer {
            adapter.amountInfo = it
        })

        viewModel.getUserAmountInfo()

        setUser(viewModel.user)
    }

    override fun onSettingItemClick(setting: Setting) {
        when (setting.icon) {
            R.drawable.ic_photo_permission -> {
                viewModel.user?.photoPermission?.let { permission ->
                    activity?.let {
                        AlbumPermissionSettingFragment.newInstance(permission).show(it.supportFragmentManager, "album_setting")
                    }
                }
            }
            R.drawable.ic_my_dating -> {
                activity?.startActivity(CommonActivity.newIntent<DatingActivity>(requireContext(), Page.VIEW_DATING_LIST))
            }
            R.drawable.ic_my_wallet -> {
                activity?.startActivity(CommonActivity.newIntent<WalletActivity>(requireContext(), Page.VIEW_MY_WALLET))
            }
            R.drawable.ic_privacy_setting -> {
                activity?.startActivity(CommonActivity.newIntent<PrivacyActivity>(requireContext(), Page.CREATE_PRIVACY_PASS))
            }
            R.drawable.ic_bind_phone -> {
                BindPhoneFragment.newInstance().show(childFragmentManager, "bindPhone")
            }
            R.drawable.ic_share -> {
                ShareFragment.newInstance().show(childFragmentManager, "share")
            }
            R.drawable.ic_user_protocol -> {
            }
            R.drawable.ic_clear_cache -> {
                activity?.startActivity(CommonActivity.newIntent<ReportActivity>(requireContext(), Page.CREATE_REPORT))
            }
            R.drawable.ic_about -> {
                activity?.startActivity(CommonActivity.newIntent<AboutActivity>(requireContext(), Page.VIEW_ABOUT))
            }
        }
    }

    override fun onSignOutClick() {
        viewModel.signOut()
        activity?.startActivity(SignupActivity.newIntent(requireContext()))
        activity?.finish()
    }

    private fun setUser(user: User?) {
        if (user == null) return
        Glide.with(this).load(user.portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
        username.text = user.nickname
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}
