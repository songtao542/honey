package com.snt.phoney.ui.main.mine

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.snt.phoney.extensions.removeList
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.extensions.startActivityForResult
import com.snt.phoney.ui.about.AboutActivity
import com.snt.phoney.ui.album.AlbumActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.privacy.PrivacyActivity
import com.snt.phoney.ui.report.ReportActivity
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

        editInfo.setOnClickListener { context?.let { context -> context.startActivity<UserActivity>(Page.EDIT_USER) } }
        upgradeVip.setOnClickListener { context?.let { context -> context.startActivity<VipActivity>(Page.VIP) } }

        viewModel.amountInfo.observe(this, Observer {
            adapter.amountInfo = it
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

        viewModel.getUserAmountInfo()
        viewModel.getUserPhotos()

        setUser(viewModel.user)
    }

    override fun onSettingItemClick(setting: Setting) {
        when (setting.icon) {
            R.drawable.ic_photo_permission -> {
                viewModel.user?.photoPermission?.let { permission ->
                    activity?.let {
                        AlbumPermissionSettingFragment.newInstance(permission).apply {
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
                                    PhotoPermission.UNLOCKED -> {
                                        showProgress(getString(R.string.on_going_seting))
                                        viewModel.setPhotoPermission(PhotoPermission.UNLOCKED)
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
                activity?.startActivity<DatingActivity>(Page.MY_DATING)
            }
            R.drawable.ic_my_wallet -> {
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
            }
            R.drawable.ic_clear_cache -> {
                activity?.startActivity<ReportActivity>(Page.REPORT)
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
        Picker.showPhotoPicker(fragment = this, max = 5)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handlePhotoPick(requestCode, resultCode, data)
        handleAlbumPhotoDelete(requestCode, resultCode, data)
    }

    private fun handlePhotoPick(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Picker.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val paths = Matisse.obtainPathResult(data)
            showProgress(getString(R.string.on_going_upload))
            viewModel.uploadPhotos(paths.map { File(it) })
        }
    }

    private fun handleAlbumPhotoDelete(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_ALBUM && resultCode == Activity.RESULT_OK) {
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

    override fun onPhotoClick(index: Int, photo: Photo) {
        startActivityForResult<AlbumActivity>(Page.ALBUM_VIEWER, REQUEST_CODE_ALBUM, Bundle().apply {
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

    private fun setUser(user: User?) {
        if (user == null) return
        Glide.with(this).load(user.portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
        username.text = user.nickname

    }


    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()

        @JvmStatic
        val REQUEST_CODE_ALBUM = 28
    }
}
