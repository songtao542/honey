package com.snt.phoney.ui.location

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.PoiAddress
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_location_picker.*
import javax.inject.Inject

/**
 *
 */
class LocationPickerFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private lateinit var viewModel: LocationViewModel

    @Inject
    lateinit var poiSearcher: PoiSearcher

    private var myLocation: Location? = null

    private lateinit var mapProxy: MapProxy

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_picker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LocationViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.location_picker_title)

        mapView.onCreate(savedInstanceState)
        if (!checkPermission()) {
            checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        val myLocationStyle = MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        myLocationStyle.showMyLocation(true)


        mapView.map.myLocationStyle = myLocationStyle
        mapView.map.isMyLocationEnabled = true


        mapProxy = MapLocationFactory.create(requireContext(), mapView = mapView)

        searchBox.setOnEditorActionListener { textView, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val keyword = textView.text?.toString() ?: ""
                    if (keyword.isNotEmpty()) {
                        search(keyword)
                    }
                    return@setOnEditorActionListener true
                }
                else -> {
                    return@setOnEditorActionListener false
                }
            }
        }

        viewModel.location.observe(this, Observer {
            myLocation = it
            setMyLocation(it, 18f)
        })

        myLocationButton.setOnClickListener {
            getMyLocation()
        }
    }


    private fun search(keyword: String) {
        if (myLocation != null) {
            myLocation?.let {
                poiSearcher.search(it, keyword).observe(this, Observer { result ->
                    showPoiSearchResult(keyword, result)
                })
            }
        } else {
            viewModel.getMyLocation { location ->
                myLocation = location
                poiSearcher.search(location, keyword).observe(this, Observer { result ->
                    showPoiSearchResult(keyword, result)
                })
            }
        }
    }

    private fun showPoiSearchResult(title: String, result: List<PoiAddress>) {
        val list = ArrayList<PoiAddress>()
        list.addAll(result)
        val fragment = childFragmentManager.findFragmentByTag("poi_result")
        if (fragment != null) {
            val sheet = fragment as LocationBottomSheetDialog
            sheet.setSearchResult(result)
            sheet.setTitle(title)
        } else {
            LocationBottomSheetDialog.newInstance(Bundle().apply {
                putParcelableArrayList(Constants.Extra.LIST, list)
                putString(Constants.Extra.TITLE, title)
            }).show(childFragmentManager, "poi_result")
        }
    }

    private fun getMyLocation() {
        viewModel.getMyLocation()
    }

    @Suppress("SENSELESS_COMPARISON")
    private fun setMyLocation(location: Location, zoomLevel: Float?) {
        if (location.latitude != null && location.longitude != null) {
            //var latLng = convertGpsToGCJ02(location.latitude!!, location.longitude!!)
            //使用高德定位之后，国内默认是GCJ02
            val latLng = LatLng(location.latitude, location.longitude)
            if (zoomLevel != null) {
                mapView.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
            } else {
                mapView.map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        mapView.onDestroy()
        super.onDestroyView()
    }


    private fun checkPermission(): Boolean {
        return context?.checkAppPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) == true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (checkPermission()) {

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = LocationPickerFragment().apply {
            this.arguments = arguments
        }
    }
}
