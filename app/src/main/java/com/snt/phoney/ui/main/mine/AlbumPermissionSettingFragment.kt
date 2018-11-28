package com.snt.phoney.ui.main.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BottomDialogFragment
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_album_permission_setting.*

/**
 */
class AlbumPermissionSettingFragment : BottomDialogFragment() {

    private lateinit var photoPermission: PhotoPermission

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
        select(photoPermission)
        publicAlbum.setOnClickListener { select(PhotoPermission.PUBLIC) }
        needCharge.setOnClickListener { select(PhotoPermission.NEED_CHARGE) }
        needApply.setOnClickListener { select(PhotoPermission.NEED_APPLY) }
        privateAlbum.setOnClickListener { select(PhotoPermission.PRIVATE) }
        confirm.setOnClickListener {

        }
    }

    private fun select(permission: PhotoPermission) {
        if (photoPermission != permission) {
            clearSelector()
            when (permission) {
                PhotoPermission.PUBLIC -> publicAlbum.isSelected = true
                PhotoPermission.NEED_CHARGE -> needCharge.isSelected = true
                PhotoPermission.NEED_APPLY -> needApply.isSelected = true
                PhotoPermission.PRIVATE -> privateAlbum.isSelected = true
            }
            photoPermission = permission
        }
    }

    private fun clearSelector() {
        publicAlbum.isSelected = false
        needCharge.isSelected = false
        needApply.isSelected = false
        privateAlbum.isSelected = false
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
