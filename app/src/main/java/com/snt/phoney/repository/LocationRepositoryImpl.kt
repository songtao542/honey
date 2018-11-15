package com.snt.phoney.repository

import android.app.Application
import com.appmattus.layercache.Cache
import com.appmattus.layercache.MapCache
import com.appmattus.layercache.jsonSerializer
import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.City
import com.snt.phoney.domain.model.Province
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.persistence.ProvinceCityDao
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.utils.cache.KeyValueDatabaseCache
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val application: Application, private val api: Api, private val provinceCityDao: ProvinceCityDao) : LocationRepository {

    private var _provinces = ArrayList<Province>()
    var provinces: List<Province>
        set(value) {
            value?.let {
                runBlocking {
                    async {
                        _provinces.clear()
                        _provinces.addAll(value)
                        provinceCityDao.insertAllProvince(_provinces)
                        return@async
                    }.await()
                }
            }
        }
        get() {
            if (_provinces.size == 0) {
                runBlocking {
                    async {
                        //先尝试从本地数据库加载
                        provinceCityDao.getProvinces()?.let {
                            _provinces.addAll(it)
                        }
                        //如果数据库中没有，则从服务器加载
                        if (_provinces.size == 0) {
                            getCities().subscribeBy {
                                it.data?.let { provinces ->
                                    _provinces.addAll(provinces)
                                }
                            }
                        }
                    }.await()
                }
            }
            return _provinces
        }


    override val cities: List<City>
        get() {
            val cities = ArrayList<City>()
            provinces.forEach {
                it.cities?.forEach { city ->
                    cities.add(city)
                }
            }
            return cities
        }


    override fun getCities(): Single<Response<List<Province>>> {
        return api.listCities()
    }
}