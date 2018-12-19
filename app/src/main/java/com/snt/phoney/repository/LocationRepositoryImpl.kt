package com.snt.phoney.repository

import android.app.Application
import android.location.Location
import android.util.Log
import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.City
import com.snt.phoney.domain.model.Province
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.persistence.ProvinceCityDao
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.utils.location.AMapLocationProvider
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationParams
import io.nlopez.smartlocation.rx.ObservableFactory
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(private val application: Application, private val api: Api, private val provinceCityDao: ProvinceCityDao) : LocationRepository {
    private var locationControl: SmartLocation.LocationControl

    init {
        val smartLocation = SmartLocation.Builder(application).logging(true).build()
        locationControl = smartLocation.location(AMapLocationProvider(application)).oneFix().config(LocationParams.NAVIGATION)
    }

    override fun getLocation(): Observable<Location> {
        return ObservableFactory.from(locationControl)
    }

    private var _provinces = ArrayList<Province>()

    /**
     * get 方法会阻塞当前线程
     */
    private var provinces: List<Province>
        set(value) {
            value?.let {
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.Default) {
                        _provinces.clear()
                        _provinces.addAll(value)
                        provinceCityDao.insertAllProvinces(_provinces)
                    }
                }
            }
        }
        get() {
            if (_provinces.size == 0) {
                runBlocking {
                    withContext(Dispatchers.Default) {
                        //先尝试从本地数据库加载
                        provinceCityDao.getProvinces()?.let {
                            _provinces.addAll(it)
                        }
                        //如果数据库中没有，则从服务器加载
                        if (_provinces.size == 0) {
                            getCities().subscribeBy {
                                it.data?.let { provinces ->
                                    Log.d("TTTT", "pppppppppppp from net===$provinces")
                                    _provinces.addAll(provinces)
                                    provinceCityDao.insertAllProvinces(_provinces)
                                }
                            }
                        }
                    }
                }
            }
            Log.d("TTTT", "pppppppppppp return===$_provinces")
            return _provinces
        }

    /**
     * get 方法会阻塞当前线程
     */
    override val cities: List<City>
        get() {
            val results = ArrayList<City>()
            runBlocking {
                withContext(Dispatchers.Default) {
                    provinces.forEach { province ->
                        val cities = provinceCityDao.getCities(province.id)
                        cities?.forEach { city ->
                            city.provinceId = province.id
                            city.provinceName = province.name
                            results.add(city)
                        }
                        province.cities = cities
                    }
                }
            }
            Log.d("TTTT", "ccccccccccccccc return===$results")
            return results
        }


    override fun getCities(): Single<Response<List<Province>>> {
        return api.listCities()
    }
}