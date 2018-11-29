package com.snt.phoney.ui.location

import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.*
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.snt.phoney.domain.model.PoiAddress
import com.snt.phoney.domain.model.Position
import javax.inject.Inject


class PoiSearcher @Inject constructor(private val application: Application) {
    fun reverseGeocode(location: Location): LiveData<PoiAddress> {
        val result = MutableLiveData<PoiAddress>()
        if (location.latitude != null && location.longitude != null) {
            val geocoderSearch = GeocodeSearch(application)
            geocoderSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult?, p1: Int) {
                    result.postValue(regeocodeResult?.regeocodeAddress?.toPoiAddress())
                }

                override fun onGeocodeSearched(geocodeResult: GeocodeResult?, p1: Int) {
                    //geocode, do nothing
                }
            })

            //var latLng = convertGpsToGCJ02(location.latitude!!, location.longitude!!)
            //使用高德定位之后，国内默认是GCJ02
            val latLng = LatLng(location.latitude!!, location.longitude!!)
            val query = RegeocodeQuery(LatLonPoint(latLng.latitude, latLng.longitude), 200f, GeocodeSearch.AMAP)
            geocoderSearch.getFromLocationAsyn(query)
        }
        return result
    }

    fun search(location: Location, keyword: String): LiveData<List<PoiAddress>> {
        val result = MutableLiveData<List<PoiAddress>>()
        val poiSearchQuery = PoiSearch.Query(keyword, "")
        val poiSearch = PoiSearch(application, poiSearchQuery)
        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiSearched(pageResult: PoiResult?, errorCode: Int) {
                pageResult?.let {
                    if (it.pois.size > 0) {
                        val pois = ArrayList<PoiAddress>()
                        for (poiItem in it.pois) {
                            pois.add(poiItem.toPoiAddress())
                        }
                        result.postValue(pois)
                    }
                }
            }

            override fun onPoiItemSearched(poiItem: PoiItem, errorCode: Int) {
            }
        })
        //var latLng = convertGpsToGCJ02(location.latitude!!, location.longitude!!)
        val latLng = LatLng(location.latitude, location.longitude)
        poiSearch.bound = PoiSearch.SearchBound(LatLonPoint(latLng.latitude, latLng.longitude), 5000)
        poiSearch.searchPOIAsyn()
        return result
    }
}

fun RegeocodeAddress.toPoiAddress(): PoiAddress {
    return PoiAddress(
            country = country,
            province = province,
            district = district,
            city = city,
            street = streetNumber?.street,
            streetNumber = streetNumber?.number,
            address = formatAddress
    )
}

fun PoiItem.toPoiAddress(): PoiAddress {
    return PoiAddress(
            title = title,
            distance = distance.toDouble(),
            position = if (latLonPoint != null) Position(latLonPoint.latitude, latLonPoint.longitude) else null,
            entrancePosition = if (enter != null) Position(enter.latitude, enter.longitude) else null,
            exitPosition = if (exit != null) Position(exit.latitude, exit.longitude) else null,
            tel = tel,
            province = provinceName,
            city = cityName,
            street = snippet,
            postalCode = postcode)
}