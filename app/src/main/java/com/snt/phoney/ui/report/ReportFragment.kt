package com.snt.phoney.ui.report

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.ReportReason
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.utils.Picker
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoFlowAdapter
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.internal.utils.PathUtils
import kotlinx.android.synthetic.main.fragment_report.*
import java.io.File

class ReportFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = ReportFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: ReportViewModel

    private lateinit var photoAdapter: PhotoFlowAdapter

    private var selectedPhotoUris = ArrayList<Uri>()

    private lateinit var targetUuid: String
    /**
     * 0 用户 1 约会
     */
    private var type: Int = 0

    private var checkedReason: ReportReason? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            targetUuid = it.getString(Constants.Extra.UUID, "")
            type = it.getInt(Constants.Extra.TYPE, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(toolbar, true)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReportViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        viewModel.reportReasons.observe(this, Observer {
            it?.let { reasons ->
                setReportReason(reasons)
            }
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        viewModel.reportSuccess.observe(this, Observer {
            snackbar(it)
        })

        photoAdapter = PhotoFlowAdapter(requireContext())
                .setMaxShow(1)
                .setAddButtonStyle(PhotoFlowAdapter.AddButtonStyle.BORDER)
                .setShowAddWhenFull(false)
                .setUris(selectedPhotoUris)
                .setLastAsAdd(true)
                .setOnAddClickListener {
                    Picker.showPhotoPicker(fragment = this@ReportFragment, max = 1)
                }

        attachments.viewAdapter = photoAdapter

        viewModel.listReportReasons()
    }

    private fun setReportReason(reasons: List<ReportReason>) {
        radioGroup.removeAllViews()
        for (reason in reasons) {
            val radio = AppCompatRadioButton(context)
            val lp = RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT)
            lp.layoutDirection = View.LAYOUT_DIRECTION_RTL
            radio.layoutParams = lp
            radio.compoundDrawablePadding = dip(10)
            val dip15 = dip(15)
            radio.setPadding(0, dip15, 0, dip15)
            radio.text = reason.name
            radio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            radio.tag = reason
            radio.setOnCheckedChangeListener { _, isChecked ->
                Log.d("TTTT", "xxxxxxxxxxxxxxxxxxxxxxgggggggggggggggggggggg")
                if (isChecked) {
                    checkedReason = radio.tag as ReportReason
                }
            }
            radioGroup.addView(radio)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.report, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.report -> {
                if (valid()) {
                    val content = contentView.text?.toString() ?: ""
                    val file = File(PathUtils.getPath(requireContext(), selectedPhotoUris[0]))
                    viewModel.report(checkedReason?.id.toString(), targetUuid, content, type, file)
                    true
                }
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun valid(): Boolean {
        if (checkedReason == null) {
            snackbar(getString(R.string.please_choose_reason))
            return false
        }
        if (selectedPhotoUris.isEmpty()) {
            snackbar(getString(R.string.please_choose_file))
            return false
        }
        if (TextUtils.isEmpty(contentView.text?.toString())) {
            snackbar(getString(R.string.please_input_reason))
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
                attachments.notifyAdapterSizeChanged()
            }
        }
    }


}
