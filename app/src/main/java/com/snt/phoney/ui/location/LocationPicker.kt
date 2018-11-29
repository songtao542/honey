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
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.PoiAddress
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_location_picker1.*
import javax.inject.Inject

/**
 *
 */
class LocationPicker : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private lateinit var viewModel: LocationViewModel

    private var myLocation: Location? = null

    private lateinit var mapProxy: MapProxy

    private lateinit var adapter: LocationPickerRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_picker1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LocationViewModel::class.java)

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        mapProxy = MapLocationFactory.create(requireContext(), mapView = mapView)

        mapProxy.onCreate(savedInstanceState)

        val myLocationStyle = MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        myLocationStyle.showMyLocation(true)

        mapView.map.myLocationStyle = myLocationStyle
        mapView.map.isMyLocationEnabled = true

        nestLayout.setMinHeight(dip(100))

        list.layoutManager = LinearLayoutManager(requireContext())
        adapter = LocationPickerRecyclerViewAdapter()
        list.adapter = adapter

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

        viewModel.myLocation.observe(this, Observer {
            myLocation = it
            setMyLocation(it, 18f)
        })

        myLocationButton.setOnClickListener {
            getMyLocation()
        }

        if (!checkPermission()) {
            checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            search("")
        }
    }


    private fun search(keyword: String) {
        if (myLocation != null) {
            myLocation?.let {
                mapProxy.searchPoi(keyword, it).observe(this, Observer { result ->
                    showPoiSearchResult(keyword, result)
                })
            }
        } else {
            viewModel.getMyLocation { location ->
                myLocation = location
                setMyLocation(location, 18f)
                mapProxy.searchPoi(keyword, location).observe(this, Observer { result ->
                    showPoiSearchResult(keyword, result)
                })
            }
        }
    }

    private fun showPoiSearchResult(title: String, result: List<PoiAddress>) {
        adapter.data = result
    }

    private fun getMyLocation() {
        viewModel.getMyLocation()?.disposedBy(disposeBag)
    }

    private fun setMyLocation(location: Location, zoomLevel: Float?) {
        mapProxy.setCenter(location, zoomLevel)
    }

    override fun onResume() {
        super.onResume()
        mapProxy.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapProxy.onPause()
    }

    override fun onDestroyView() {
        mapProxy.onDestroy()
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
        fun newInstance(arguments: Bundle? = null) = LocationPicker().apply {
            this.arguments = arguments
        }
    }
}
