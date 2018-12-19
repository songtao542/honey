package com.snt.phoney.domain.persistence

import androidx.room.*
import com.snt.phoney.domain.model.City
import com.snt.phoney.domain.model.Province

@Dao
abstract class ProvinceCityDao {
    @Query("SELECT * FROM CITY")
    internal abstract fun getCities(): List<City>?

    @Query("SELECT * FROM CITY WHERE provinceId=:provinceId")
    internal abstract fun getCities(provinceId: Int): List<City>?

    @Transaction
    @Query("SELECT * FROM PROVINCE")
    internal abstract fun getProvinces(): List<Province>?

    @Query("DELETE FROM PROVINCE")
    internal abstract fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract fun insertProvince(province: Province)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract fun insertProvinces(provinces: List<Province>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract fun insertCities(cities: List<City>)

    @Transaction
    open fun insertAllProvinces(provinces: List<Province>) {
        insertProvinces(provinces)
        provinces.forEach { province ->
            province.cities?.let { cities ->
                cities.forEach { city ->
                    city.provinceId = province.id
                    city.provinceName = province.name
                }
                insertCities(cities)
            }
        }
    }
}