package com.snt.phoney.ui.dating.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.domain.model.PoiAddress
import com.snt.phoney.extensions.setSoftInputMode
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.location.LocationPicker
import com.snt.phoney.utils.Picker
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoFlowAdapter
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.fragment_dating_create.*
import java.io.File

class CreateDatingFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = CreateDatingFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: CreateDatingViewModel

    private lateinit var photoAdapter: PhotoFlowAdapter
    private var selectedPhotos = ArrayList<String>()

    private var selectedAddress: PoiAddress? = null
    private var selectedProgram: DatingProgram? = null
    private var selectedProgramIndex: Int = -1
    private var selectedDay: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_dating_create, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(toolbar)
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateDatingViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        selectDatingAddress.setOnClickListener {
            activity?.let { activity ->
                val locationPicker = LocationPicker.newInstance()
                locationPicker.setOnResultListener { address ->
                    selectedAddress = address
                    datingAddress.text = address.title ?: address.formatAddress
                }
                (activity as CommonActivity).addFragmentSafely(locationPicker, "location_picker", addToBackStack = true)
                return@let
            }
        }
        selectDatingTime.setOnClickListener {
            //            Picker.showDatePicker(activity) { year, monthOfYear, dayOfMonth ->
//                datingTime.text = getString(R.string.dating_date_template, year, monthOfYear + 1, dayOfMonth)//"${year}年${monthOfYear}月${dayOfMonth}日"
//            }
            Picker.showPicker(activity, getString(R.string.select_dating_time), 1, 20, selectedDay,
                    "days_picker") { value, _ ->
                selectedDay = value
                datingTime.text = getString(R.string.days_template, value)
            }
        }
        selectDatingProgram.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.select_dating_program), 0, "select_program", { picker ->
                viewModel.programs.observe(this, Observer { programs ->
                    val programNames = Array(programs.size) { index ->
                        programs[index].name!!
                    }
                    picker.setColumn(programNames)
                })
            }) { value1, _ ->
                selectedProgramIndex = value1
                selectedProgram = viewModel.programs.value?.get(value1)
                datingProgram.text = selectedProgram?.name
            }
        }
        photoAdapter = PhotoFlowAdapter(requireContext())
                .setMaxShow(8)
                .setAddButtonStyle(PhotoFlowAdapter.AddButtonStyle.BORDER)
                .setShowAddWhenFull(false)
                .setUrls(selectedPhotos)
                .setLastAsAdd(true)
                .setOnAddClickListener {
                    Picker.showPhotoPicker(fragment = this@CreateDatingFragment, max = 8)
                }

        photos.viewAdapter = photoAdapter
        photos.setOnItemClickListener { _, index ->
            (activity as CommonActivity).addFragmentSafely(Page.PHOTO_VIEWER, "photo_viewer", true, argument = Bundle().apply {
                putStringArrayList(Constants.Extra.LIST, selectedPhotos)
                putInt(Constants.Extra.INDEX, index)
            })
        }

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })
        viewModel.success.observe(this, Observer {
            snackbar(getString(R.string.publish_success))
            activity?.finish()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_dating, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.publishDatingConfirm -> {
                if (isValid()) {
                    val content = datingContent.text?.toString() ?: ""
                    val cover = selectedPhotos.map { File(it) }
                    viewModel.publish("", selectedProgram!!.safeName, content, selectedDay, selectedAddress!!, cover)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isValid(): Boolean {
        if (selectedAddress == null || selectedProgram == null || selectedDay == 0 || selectedPhotos.isEmpty()) {
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
            paths?.let {
                selectedPhotos.addAll(paths)
                photos.notifyAdapterSizeChanged()
            }
        }
    }

}
