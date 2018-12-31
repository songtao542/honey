package com.snt.phoney.ui.main.mine

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.Page
import com.snt.phoney.base.ProgressDialog
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.model.UserInfo
import com.snt.phoney.extensions.removeList
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.extensions.startActivityForResult
import com.snt.phoney.ui.about.AboutActivity
import com.snt.phoney.ui.album.AlbumActivity
import com.snt.phoney.ui.browser.WebBrowserActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.privacy.PrivacyActivity
import com.snt.phoney.ui.setup.BindPhoneFragment
import com.snt.phoney.ui.share.ShareFragment
import com.snt.phoney.ui.signup.SignupActivity
import com.snt.phoney.ui.user.UserActivity
import com.snt.phoney.ui.vip.VipActivity
import com.snt.phoney.ui.wallet.WalletActivity
import com.snt.phoney.utils.Picker
import com.snt.phoney.utils.data.Constants
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.fragment_mine_header.*
import kotlinx.android.synthetic.main.fragment_mine_list.*
import java.io.File


const val REQUEST_AUTH_CODE = 56
const val REQUEST_ALBUM_CODE = 58
const val REQUEST_HEAD_ICON_CODE = 59
const val REQUEST_VIP_CODE = 60

/**
 * A fragment representing a list of Items.
 */
class MineFragment : BaseFragment(), OnSettingItemClickListener, OnSignOutClickListener,
        OnAddPhotoClickListener,
        OnPhotoClickListener {

    lateinit var viewModel: MineViewModel

    lateinit var adapter: MineRecyclerViewAdapter

    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MineViewModel::class.java)

        list.layoutManager = LinearLayoutManager(context)
        adapter = MineRecyclerViewAdapter(this@MineFragment)
        adapter.setOnSettingItemClickListener(this)
        adapter.setOnAddPhotoClickListener(this)
        adapter.setOnPhotoClickListener(this)
        adapter.setOnSignOutClickListener(this)
        list.adapter = adapter

        viewModel.userInfo.observe(this, Observer {
            adapter.userInfo = it
            setUserInfo(it)
        })

        viewModel.success.observe(this, Observer {
            dismissProgress()
            snackbar(it)
        })
        viewModel.toast.observe(this, Observer {
            dismissProgress()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        viewModel.error.observe(this, Observer {
            dismissProgress()
            snackbar(it)
        })

        viewModel.photos.observe(this, Observer {
            adapter.photos = it
            adapter.notifyDataSetChanged()
        })

        editInfo.setOnClickListener { context?.let { context -> context.startActivity<UserActivity>(Page.EDIT_USER) } }

        head.setOnClickListener {
            Picker.showPhotoPicker(fragment = this, max = 1, crop = true, requestCode = REQUEST_HEAD_ICON_CODE)
        }

        viewModel.getUserPhotos()

        viewModel.user.observe(this, Observer {
            setUser(it)
            //用户信息变更之后重新网络加载用户信息
            viewModel.getAllInfoOfUser()
        })
    }

    override fun onSettingItemClick(setting: Setting) {
        when (setting.icon) {
            R.drawable.ic_photo_permission -> {
                viewModel.user.value?.photoPermission?.let { initPermission ->
                    activity?.let {
                        AlbumPermissionSettingFragment.newInstance(initPermission).apply {
                            setOnSelectListener { permission ->
                                when (permission) {
                                    PhotoPermission.NEED_CHARGE -> {
                                        activity?.let { activity ->
                                            activity.startActivity<AlbumActivity>(Page.PAY_SETTING, Bundle().apply {
                                                putInt(Constants.Extra.PERMISSION, PhotoPermission.NEED_CHARGE.value)
                                                putParcelableArrayList(Constants.Extra.PHOTO_LIST, ArrayList<Photo>(viewModel.photos.value))
                                            })
                                        }
                                    }
                                    PhotoPermission.PUBLIC -> {
                                        showProgress(getString(R.string.on_going_seting))
                                        viewModel.setPhotoPermission(PhotoPermission.PUBLIC)
                                    }
                                    PhotoPermission.LOCKED -> {
                                        showProgress(getString(R.string.on_going_seting))
                                        viewModel.setPhotoPermission(PhotoPermission.LOCKED)
                                    }
                                    PhotoPermission.NEED_APPLY -> {
                                        showProgress(getString(R.string.on_going_seting))
                                        viewModel.setPhotoPermission(PhotoPermission.NEED_APPLY)
                                    }
                                    PhotoPermission.PRIVATE -> {
                                        showProgress(getString(R.string.on_going_seting))
                                        viewModel.setPhotoPermission(PhotoPermission.PRIVATE)
                                    }
                                }
                            }
                        }.show(it.supportFragmentManager, "album_setting")
                    }
                }
            }
            R.drawable.ic_my_dating -> {
                if (setting.hasNewMessage) {
                    setting.hasNewMessage = false
                    viewModel.userInfo.value?.hasNewsOfDating = false
                    adapter.notifyDataSetChanged()
                }
                activity?.startActivity<DatingActivity>(Page.MY_DATING)
            }
            R.drawable.ic_my_wallet -> {
                if (setting.hasNewMessage) {
                    viewModel.setWalletNewsToRead()
                    setting.hasNewMessage = false
                    adapter.notifyDataSetChanged()
                }
                activity?.startActivity<WalletActivity>(Page.WALLET)
            }
            R.drawable.ic_privacy_setting -> {
                context?.let { context ->
                    AlertDialog.Builder(context)
                            .setTitle(R.string.modify_privacy_lock)
                            .setMessage(R.string.modify_privacy_lock_message)
                            .setNegativeButton(R.string.cancel) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(R.string.confirm) { dialog, _ ->
                                dialog.dismiss()
                                activity?.startActivity<PrivacyActivity>(Page.CREATE_PRIVACY_LOCK)
                            }.show()

                }
            }
            R.drawable.ic_bind_phone -> {
                BindPhoneFragment.newInstance().show(childFragmentManager, "bindPhone")
            }
            R.drawable.ic_share -> {
                ShareFragment.newInstance().show(childFragmentManager, "share")
            }
            R.drawable.ic_user_protocol -> {
                startActivity<WebBrowserActivity>(Bundle().apply {
                    putString(Constants.Extra.TITLE, getString(R.string.user_protocol))
                    putString(Constants.Extra.URL, Constants.Api.USER_PROTOCOL_URL)
                })
            }
            R.drawable.ic_clear_cache -> {
                //activity?.startActivity<ReportActivity>(Page.REPORT)
            }
            R.drawable.ic_about -> {
                activity?.startActivity<AboutActivity>(Page.ABOUT)
            }
        }
    }

    private fun showProgress(tip: String) {
        progressDialog = ProgressDialog.newInstance(tip)
        progressDialog!!.show(childFragmentManager, "progress")
    }

    private fun dismissProgress() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun onAddPhotoClick() {
        val max = 19 - (viewModel.photos.value?.size ?: 0)
        Picker.showPhotoPicker(fragment = this, max = Math.min(max, 5))
    }

    override fun onPhotoClick(index: Int, photo: Photo) {
        startActivityForResult<AlbumActivity>(Page.ALBUM_VIEWER, REQUEST_ALBUM_CODE, Bundle().apply {
            putParcelableArrayList(Constants.Extra.PHOTO_LIST, ArrayList<Photo>(viewModel.photos.value))
            putInt(Constants.Extra.INDEX, index)
            putBoolean(Constants.Extra.DELETABLE, true)
        })
    }

    override fun onSignOutClick() {
        viewModel.signOut()
        activity?.startActivity(SignupActivity.newIntent(requireContext()))
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handlePhotoPick(requestCode, resultCode, data)
        handleAlbumPhotoDelete(requestCode, resultCode, data)
        handleAuthResult(requestCode, resultCode, data)
        handleHeadIconResult(requestCode, resultCode, data)
        handleVipResult(requestCode, resultCode, data)
    }

    private fun handlePhotoPick(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Picker.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            data?.let { data ->
                val paths = Matisse.obtainPathResult(data)
                showProgress(getString(R.string.on_going_upload))
                viewModel.uploadPhotos(paths.map { File(it) })
            }
        }
    }

    private fun handleHeadIconResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_HEAD_ICON_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val paths = Matisse.obtainPathResult(data)
                if (paths.isNotEmpty()) {
                    showProgress(getString(R.string.on_going_upload))
                    viewModel.uploadHeadIcon(File(paths[0]))
                }
            }
        }
    }

    private fun handleAlbumPhotoDelete(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ALBUM_CODE && resultCode == Activity.RESULT_OK) {
            data?.let { data ->
                val delete = data.getParcelableArrayListExtra<Photo>(Constants.Extra.LIST)
                val photos = viewModel.photos.value
                photos?.let { photos ->
                    if (delete != null && delete.isNotEmpty()) {
                        viewModel.photos.value = ArrayList<Photo>(photos).removeList(delete)
                    }
                }
                return@let
            }
        }
    }

    private fun handleAuthResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_AUTH_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val success = data.getBooleanExtra(Constants.Extra.DATA, false)
                if (success) {
                    viewModel.getAllInfoOfUser()
                }
            }
        }
    }

    private fun handleVipResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_VIP_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val success = data.getBooleanExtra(Constants.Extra.DATA, false)
                if (success) {
                    viewModel.getAllInfoOfUser()
                }
            }
        }
    }

    private fun setUser(user: User?) {
        if (user == null) return
        Glide.with(this).load(user.avatar).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
        username.text = user.nickname
    }

    private fun setUserInfo(userInfo: UserInfo?) {
        userInfo?.let { userInfo ->
            userInfo.authState?.let { authState ->
                when (authState.state) {
                    2 -> authInfo.text = authState.score
                    else -> authInfo.text = authState.message
                }
            }
            userInfo.vipInfo?.let { vipInfo ->
                if (vipInfo.isVip) {
                    vipType.setText(R.string.vip_member)
                    upgradeVip.visibility = View.GONE
                } else {
                    vipType.setText(R.string.normal_member)
                    upgradeVip.setText(R.string.upgrade_vip_title)
                    upgradeVip.setOnClickListener { startActivityForResult<VipActivity>(Page.VIP, REQUEST_VIP_CODE) }
                }
            }
            return@let
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}
