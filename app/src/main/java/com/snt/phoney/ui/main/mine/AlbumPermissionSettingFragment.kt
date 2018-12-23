package com.snt.phoney.ui.main.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BottomDialogFragment
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.ui.album.AlbumSettingActivity
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_album_permission_setting.*

/**
 */
class AlbumPermissionSettingFragment : BottomDialogFragment() {

    private lateinit var photoPermission: PhotoPermission

    private lateinit var viewModel: MineViewModel

    private var listener: ((permission: PhotoPermission) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoPermission = PhotoPermission.from(it.getInt(Constants.Extra.PERMISSION))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_album_permission_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MineViewModel::class.java)
        publicAlbum.background = resources.getDrawable(R.drawable.album_permission_setting_item_selector, activity?.theme)
        needCharge.background = resources.getDrawable(R.drawable.album_permission_setting_item_selector, activity?.theme)
        needApply.background = resources.getDrawable(R.drawable.album_permission_setting_item_selector, activity?.theme)
        privateAlbum.background = resources.getDrawable(R.drawable.album_permission_setting_item_selector, activity?.theme)
        setSelect(photoPermission)
        publicAlbum.setOnClickListener { select(PhotoPermission.PUBLIC) }
        needCharge.setOnClickListener { select(PhotoPermission.NEED_CHARGE) }
        needApply.setOnClickListener { select(PhotoPermission.NEED_APPLY) }
        privateAlbum.setOnClickListener { select(PhotoPermission.PRIVATE) }
        confirm.setOnClickListener {
            dismiss()
            when (photoPermission) {
                PhotoPermission.NEED_CHARGE -> {
                    listener?.invoke(PhotoPermission.NEED_CHARGE)
                }
                PhotoPermission.PUBLIC -> {
                    listener?.invoke(PhotoPermission.PUBLIC)
                }
                PhotoPermission.UNLOCKED -> {
                    listener?.invoke(PhotoPermission.UNLOCKED)
                }
                PhotoPermission.NEED_APPLY -> {
                    listener?.invoke(PhotoPermission.NEED_APPLY)
                }
                PhotoPermission.PRIVATE -> {
                    listener?.invoke(PhotoPermission.PRIVATE)
                }
            }
        }
    }

    private fun select(permission: PhotoPermission) {
        if (photoPermission != permission) {
            setSelect(permission)
        }
    }

    private fun setSelect(permission: PhotoPermission) {
        clearSelector()
        when (permission) {
            PhotoPermission.PUBLIC -> publicAlbum.isSelected = true
            PhotoPermission.NEED_CHARGE -> needCharge.isSelected = true
            PhotoPermission.NEED_APPLY -> needApply.isSelected = true
            PhotoPermission.PRIVATE -> privateAlbum.isSelected = true
        }
        photoPermission = permission
    }

    private fun clearSelector() {
        publicAlbum.isSelected = false
        needCharge.isSelected = false
        needApply.isSelected = false
        privateAlbum.isSelected = false
    }

    fun setOnSelectListener(listener: ((permission: PhotoPermission) -> Unit)) {
        this.listener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance(photoPermission: Int) = AlbumPermissionSettingFragment().apply {
            this.arguments = Bundle().apply {
                putInt(Constants.Extra.PERMISSION, photoPermission)
            }
        }

        @JvmStatic
        fun newInstance(arguments: Bundle?) = AlbumPermissionSettingFragment().apply {
            this.arguments = arguments
        }
    }
}
