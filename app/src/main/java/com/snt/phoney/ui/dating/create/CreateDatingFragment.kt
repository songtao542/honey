package com.snt.phoney.ui.dating.create

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.ui.location.LocationPicker
import com.snt.phoney.ui.location.LocationPickerFragment
import com.snt.phoney.utils.Picker
import com.snt.phoney.widget.PhotoFlowAdapter
import kotlinx.android.synthetic.main.fragment_dating_create.*
import kotlinx.android.synthetic.main.fragment_signup_3.*
import kotlinx.android.synthetic.main.fragment_user_info_header.*

class CreateDatingFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = CreateDatingFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: CreateDatingViewModel

    private lateinit var photoAdapter: PhotoFlowAdapter
    private var photoUrls = ArrayList<String>()

    private var selectedProgram: DatingProgram? = null
    private var selectedProgramIndex: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_dating_create, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(toolbar)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateDatingViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        selectDatingAddress.setOnClickListener {
            activity?.let { activity ->
                val locationPicker = LocationPicker.newInstance()
                locationPicker.setOnResultListener { address ->
                    datingAddress.text = address.title ?: address.formatAddress
                }
                (activity as CommonActivity).addFragmentSafely(locationPicker, "location_picker", addToBackStack = true)
                return@let
            }
        }
        selectDatingTime.setOnClickListener {

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
                .setUrls(photoUrls)
                .setLastAsAdd(true)
                .setOnAddClickListener {
                    photoUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543427137051&di=980490a9ba8764b0daf759c9ab55a760&imgtype=0&src=http%3A%2F%2Fimg2.woyaogexing.com%2F2017%2F08%2F17%2F96b585776aadaa67%2521400x400_big.jpg")
                    photos.notifyAdapterSizeChanged()
                }

        photos.viewAdapter = photoAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.create_dating, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.publishDatingConfirm -> {
                Log.d("TTTT", "Confirm publish")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
