package com.snt.phoney.ui.dating.create

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.ProgressDialog
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.domain.model.PoiAddress
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.replaceFragmentSafely
import com.snt.phoney.extensions.setSoftInputMode
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.dating.detail.DatingDetailFragment
import com.snt.phoney.ui.location.LocationPicker
import com.snt.phoney.ui.photo.PhotoViewerFragment
import com.snt.phoney.utils.Picker
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.FlowLayout
import com.snt.phoney.widget.PhotoFlowAdapter
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.internal.utils.PathUtils
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_dating_create.*
import java.io.File

class CreateDatingFragment : BaseFragment(), Toolbar.OnMenuItemClickListener {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = CreateDatingFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: CreateDatingViewModel

    private lateinit var photoAdapter: PhotoFlowAdapter
    private var selectedPhotoUris = ArrayList<Uri>()

    private var selectedAddress: PoiAddress? = null
    private var selectedProgram: DatingProgram? = null
    private var selectedProgramIndex: Int = -1
    private var selectedDay: Int = 0
    private var progressDialog: ProgressDialog? = null

    private lateinit var photoView: FlowLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_dating_create, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateDatingViewModel::class.java)
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        enableOptionsMenu(toolbar, false, R.menu.create_dating)
        toolbar.setOnMenuItemClickListener(this)

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.publish_dating_title)

        photoView = photos
        selectDatingAddress.setOnClickListener {
            activity?.let { activity ->
                val locationPicker = LocationPicker.newInstance()
                locationPicker.setOnResultListener { address ->
                    selectedAddress = address
                    datingAddress.text = address.title ?: address.formatAddress
                }
                activity?.addFragmentSafely(locationPicker, "location_picker", addToBackStack = true)
                return@let
            }
        }
        selectDatingTime.setOnClickListener {
            //Picker.showDatePicker(activity) { year, monthOfYear, dayOfMonth ->
            //    datingTime.text = getString(R.string.dating_date_template, year, monthOfYear + 1, dayOfMonth)//"${year}年${monthOfYear}月${dayOfMonth}日"
            //}
            Picker.showPicker(activity, getString(R.string.select_dating_time), 1, 20, selectedDay) { value, _ ->
                selectedDay = value
                datingTime.text = getString(R.string.days_template, value)
            }
        }
        selectDatingProgram.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.select_dating_program), 0, { picker ->
                if (viewModel.programs.value != null) {
                    val programs = viewModel.programs.value!!
                    val programNames = Array(programs.size) { index ->
                        programs[index].name!!
                    }
                    picker.setColumn(programNames)
                } else {
                    viewModel.programs.observe(this, Observer { programs ->
                        val programNames = Array(programs.size) { index ->
                            programs[index].name!!
                        }
                        picker.setColumn(programNames)
                    })
                }
            }, handler = { value1, _ ->
                selectedProgramIndex = value1
                selectedProgram = viewModel.programs.value?.get(value1)
                datingProgram.text = selectedProgram?.name
            })
        }
        photoAdapter = PhotoFlowAdapter(requireContext())
                .setMaxShow(8)
                .setAddButtonStyle(PhotoFlowAdapter.AddButtonStyle.BORDER)
                .setShowAddWhenFull(false)
                .setUris(selectedPhotoUris)
                .setLastAsAdd(true)
                .setOnAddClickListener {
                    Picker.showPhotoPicker(fragment = this@CreateDatingFragment, max = 8)
                }

        photos.viewAdapter = photoAdapter
        photos.setOnItemClickListener { _, index ->
            val fragment = PhotoViewerFragment.newInstance(Bundle().apply {
                putParcelableArrayList(Constants.Extra.URI_LIST, selectedPhotoUris)
                putInt(Constants.Extra.INDEX, index)
                putBoolean(Constants.Extra.DELETABLE, true)
            }).apply {
                setOnUriResultListener { deleted ->
                    selectedPhotoUris.removeAll(deleted)
                    photoView?.notifyAdapterSizeChanged()
                }
            }
            activity?.addFragmentSafely(fragment, "photo_viewer", true)
        }

        confirmButton.setOnClickListener {
            val fragment = DatingDetailFragment.newInstance(Bundle().apply {
                putString(Constants.Extra.UUID, viewModel.success.value)
            })
            activity?.replaceFragmentSafely(fragment, "dating_detail")
        }

        viewModel.error.observe(this, Observer {
            progressDialog?.dismiss()
            snackbar(it)
        })
        viewModel.success.observe(this, Observer {
            toolbar.menu.clear()
            progressDialog?.dismiss()
            publishLayout.visibility = View.GONE
            successLayout.visibility = View.VISIBLE
            snackbar(getString(R.string.publish_success))
        })
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.create_dating, menu)
//    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.publishDatingConfirm -> {
                if (isValid()) {
                    progressDialog = ProgressDialog().cancelable(false).also { it.show(childFragmentManager, "progress") }
                    val content = datingContent.text?.toString() ?: ""
                    val cover = selectedPhotoUris.map { File(PathUtils.getPath(requireContext(), it)) }
                    viewModel.publish("", selectedProgram!!.safeName, content, selectedDay, selectedAddress!!, cover)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isValid(): Boolean {
        if (selectedAddress == null || selectedProgram == null || selectedDay == 0 || selectedPhotoUris.isEmpty()) {
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handlePhotoPick(requestCode, resultCode, data)
    }

    private fun handlePhotoPick(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Picker.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val paths = Matisse.obtainPathResult(data)
            val uris = Matisse.obtainResult(data)
            paths?.let {
                selectedPhotoUris.addAll(uris)
                photos?.notifyAdapterSizeChanged()
                return@let
            }
        }
    }

}
