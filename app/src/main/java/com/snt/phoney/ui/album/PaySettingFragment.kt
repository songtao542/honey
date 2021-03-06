package com.snt.phoney.ui.album

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.AlertDialogFragment
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.model.PhotoPermission
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.setSoftInputMode
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.main.mine.AlbumPermissionSettingFragment
import com.snt.phoney.utils.Picker
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.CompatRadioButton
import com.snt.phoney.widget.itemdecoration.MonospacedItemDecoration
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_pay_setting.*

class PaySettingFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = PaySettingFragment().apply {
            this.arguments = arguments
        }

        @JvmStatic
        fun newInstance(photoPermission: Int) = AlbumPermissionSettingFragment().apply {
            this.arguments = Bundle().apply {
                putInt(Constants.Extra.PERMISSION, photoPermission)
            }
        }
    }

    private lateinit var photoPermission: PhotoPermission

    private var photos: List<Photo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoPermission = PhotoPermission.from(it.getInt(Constants.Extra.PERMISSION))
            photos = it.getParcelableArrayList(Constants.Extra.PHOTO_LIST)
        }
    }

    private lateinit var viewModel: PaySettingViewModel
    private lateinit var adapter: PaySettingRecyclerViewAdapter

    private var checked: CompatRadioButton? = null

    private var amount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pay_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PaySettingViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.album_permission_setting)

        adapter = PaySettingRecyclerViewAdapter(this)
        list.adapter = adapter
        list.addItemDecoration(MonospacedItemDecoration(dip(8)))//VerticalDividerItemDecoration.Builder(requireContext()).size(dip(8)).build())//MonospacedItemDecoration(dip(8))
        list.layoutManager = GridLayoutManager(requireContext(), 4)

        viewModel.photos.observe(this, Observer {
            adapter.data = it
        })

        viewModel.success.observe(this, Observer {
            dismissProgress()
            snackbar(it)
            view?.postDelayed({
                finish()
            }, 500)
        })

        viewModel.error.observe(this, Observer {
            dismissProgress()
            snackbar(it)
        })

        inputPrice.setOnClickListener {
            showMibiPicker()
        }

        needPay.setOnClickListener { view ->
            setCheck(view)
        }

        unlockAll.setOnClickListener { view ->
            setCheck(view)
        }

        if (photoPermission == PhotoPermission.LOCKED) {
            setCheck(unlockAll)
        } else {
            setCheck(needPay)
        }

        confirm.setOnClickListener {
            if (checked == needPay) {
                //val amount = getAmount()
                if (amount > 0) {
                    if (adapter.selectedPhoto != null) {
                        val photoId = adapter.selectedPhoto?.id?.toString() ?: ""
                        showProgress(getString(R.string.on_going_seting))
                        viewModel.setPhotoPermission(PhotoPermission.NEED_CHARGE, amount.toDouble(), photoId)
                    } else {
                        snackbar(getString(R.string.must_select_photo))
                    }
                } else {
                    snackbar(getString(R.string.please_select_mibi_amount))
                }
            } else {
                showProgress()
                viewModel.getPhotosPrice()
            }
        }

        viewModel.photoPrice.observe(this, Observer { message ->
            dismissProgress()
            context?.let { ctx ->
                AlertDialogFragment.Builder(ctx)
                        .setTitle(R.string.notice)
                        .setMessage(message)
                        .setCancelable(false)
                        .setNegativeButton(R.string.cancel) { dialog ->
                            dialog.dismiss()
                        }.setPositiveButton(R.string.confirm) { dialog ->
                            dialog.dismiss()
                            showProgress(getString(R.string.on_going_seting))
                            viewModel.setPhotoPermission(PhotoPermission.LOCKED)
                        }.show(childFragmentManager)
                return@let
            }
        })

        if (photos == null) {
            viewModel.getUserPhotos()
        } else {
            adapter.data = photos
        }
    }

    fun finish() {
        if (viewModel.permission != -1) {
            val data = Intent()
            data.putExtra(Constants.Extra.DATA, viewModel.permission)
            activity?.setResult(Activity.RESULT_OK, data)
        }
        activity?.finish()
    }

    private fun getAmount(): Double {
        val text = price.text?.toString()
        var amount = 0.0
        if (!TextUtils.isEmpty(text)) {
            try {
                amount = text?.toDouble() ?: 0.0
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return if (amount <= 0.0) {
            snackbar(getString(R.string.price_must_input))
            -1.0
        } else {
            amount
        }
    }

    private fun showMibiPicker() {
        Picker.showPicker(activity, getString(R.string.mibi_picker_hint), 1, 80, if (amount == 0) 10 else amount) { value, _ ->
            price.text = getString(R.string.amout_mibi_template, value.toString())
            amount = value
        }
    }

    private fun setCheck(view: View) {
        if (view != checked) {
            if (view == needPay) {
                needPay.isChecked = true
                unlockAll.isChecked = false
                inputPrice.visibility = View.VISIBLE
                list.visibility = View.VISIBLE
                checked = needPay
            } else {
                needPay.isChecked = false
                unlockAll.isChecked = true
                inputPrice.visibility = View.GONE
                list.visibility = View.GONE
                checked = unlockAll
            }
        }
    }

}
