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
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.base.ProgressDialog
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.removeList
import com.snt.phoney.extensions.snackbar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        editInfo.setOnClickListener { context?.let { context -> startActivity(CommonActivity.newIntent<UserActivity>(context, Page.EDIT_USER_INFO)) } }
        upgradeVip.setOnClickListener { context?.let { context -> startActivity(CommonActivity.newIntent<VipActivity>(context, Page.UPGRADE_VIP)) } }

        viewModel.amountInfo.observe(this, Observer {
            adapter.amountInfo = it
        })

        viewModel.success.observe(this, Observer {
            progressDialog?.dismiss()
            progressDialog = null
            snackbar(it)
        })
        viewModel.toast.observe(this, Observer {
            progressDialog?.dismiss()
            progressDialog = null
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        viewModel.error.observe(this, Observer {
            progressDialog?.dismiss()
            progressDialog = null
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
                                            activity.startActivity(CommonActivity.newIntent<AlbumActivity>(activity, Page.PAY_SETTING, Bundle().apply {
                                                putInt(Constants.Extra.PERMISSION, PhotoPermission.NEED_CHARGE.value)
                                                putParcelableArrayList(Constants.Extra.PHOTO_LIST, ArrayList<Photo>(viewModel.photos.value))
                                            }))
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
                activity?.startActivity(CommonActivity.newIntent<DatingActivity>(requireContext(), Page.VIEW_MY_DATING))
            }
            R.drawable.ic_my_wallet -> {
                activity?.startActivity(CommonActivity.newIntent<WalletActivity>(requireContext(), Page.VIEW_MY_WALLET))
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
                                activity?.startActivity(CommonActivity.newIntent<PrivacyActivity>(requireContext(), Page.CREATE_PRIVACY_PASS))
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
                activity?.startActivity(CommonActivity.newIntent<ReportActivity>(requireContext(), Page.CREATE_REPORT))
            }
            R.drawable.ic_about -> {
                activity?.startActivity(CommonActivity.newIntent<AboutActivity>(requireContext(), Page.VIEW_ABOUT))
            }
        }
    }

    private fun showProgress(tip: String) {
        progressDialog = ProgressDialog.newInstance(tip)
        progressDialog!!.show(childFragmentManager, "progress")
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
        Log.d("TTTT", "hhhhhhhhhhhhhhhhhh handleAlbumPhotoDelete data=$data")
        if (requestCode == REQUEST_CODE_ALBUM && resultCode == Activity.RESULT_OK) {
            data?.let { data ->
                val delete = data.getParcelableArrayListExtra<Photo>(Constants.Extra.LIST)
                Log.d("TTTT", "hhhhhhhhhhhhhhhhhh handleAlbumPhotoDelete delete=$delete")
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
//        val fragment = PhotoViewerFragment.newInstance(Bundle().apply {
//            putParcelableArrayList(Constants.Extra.PHOTO_LIST, ArrayList<Photo>(viewModel.photos.value))
//            putInt(Constants.Extra.INDEX, index)
//            putBoolean(Constants.Extra.DELETABLE, true)
//        }).apply {
//
//        }
//        activity?.addFragmentSafely(android.R.id.content, fragment, "photo_viewer", true,
//                enterAnimation = R.anim.slide_in_up, popExitAnimation = R.anim.slide_out_down)
        //enterAnimation = R.anim.slide_in_right, popExitAnimation = R.anim.slide_out_right
        context?.let { context ->
            startActivityForResult(CommonActivity.newIntent<AlbumActivity>(context, Page.VIEW_ALBUM, Bundle().apply {
                putParcelableArrayList(Constants.Extra.PHOTO_LIST, ArrayList<Photo>(viewModel.photos.value))
                putInt(Constants.Extra.INDEX, index)
                putBoolean(Constants.Extra.DELETABLE, true)
            }), REQUEST_CODE_ALBUM)
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


    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()

        @JvmStatic
        val REQUEST_CODE_ALBUM = 28
    }
}
