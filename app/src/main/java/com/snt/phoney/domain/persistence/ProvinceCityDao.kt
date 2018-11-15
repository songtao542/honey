package com.snt.phoney.domain.persistence

import androidx.room.*
import com.snt.phoney.domain.model.City
import com.snt.phoney.domain.model.Province
import com.snt.phoney.domain.model.User

@Dao
interface ProvinceCityDao {
    @Query("SELECT * FROM CITY")
    fun getCities(): List<City>?

    @Query("SELECT * FROM CITY WHERE provinceId=:provinceId")
    fun getCities(provinceId: Int): List<City>?

    @Transaction
    @Query("SELECT * FROM PROVINCE")
    fun getProvinces(): List<Province>?

    @Query("DELETE FROM PROVINCE")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProvince(province: Province)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProvince(provinces: List<Province>)
}