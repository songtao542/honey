package com.snt.phoney.ui.location

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import com.amap.api.maps.TextureMapView
import com.snt.phoney.domain.model.PoiAddress
import com.snt.phoney.utils.location.AMapLocationProvider
import io.nlopez.smartlocation.location.LocationProvider


interface MapProxy {
    fun initialize(mapView: View) {
        //default do nothing
    }

    fun addMarker(marker: Marker, zoomLevel: Float? = null)

    fun addMarkers(markers: List<Marker>, zoomLevel: Float? = null)

    fun removeMarker(marker: Marker)

    fun removeMarkers(markers: List<Marker>)

    fun setCenter(location: Location, zoomLevel: Float? = null)

    fun onResume()

    fun onPause()

    fun onCreate(savedInstanceState: Bundle?) {
        //default do nothing
    }

    fun onSaveInstanceState(outState: Bundle) {
        //default do nothing
    }

    fun onDestroy() {
        //default do nothing
    }

    fun reverseGeocode(location: Location): LiveData<PoiAddress>

    fun searchPoi(keyword: String, location: Location): LiveData<List<PoiAddress>>

    fun getMapView(): View?
}

object MapLocationFactory {

    private var locationProvider: LocationProvider? = null

    @JvmStatic
    fun getLocationProvider(context: Context): LocationProvider? {
        if (locationProvider == null) {
            locationProvider = AMapLocationProvider(context.applicationContext)
        }
        return locationProvider
    }

    @JvmStatic
    fun create(context: Context): MapProxy {
        return AMapProxy(context.applicationContext).apply {
            initialize(TextureMapView(context))
        }
    }

}

