package com.snt.phoney.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity(foreignKeys = [
    ForeignKey(entity = Province::class,
            parentColumns = ["id"],
            childColumns = ["provinceId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
    )
], tableName = "City", indices = [Index(value = ["provinceId"], unique = true)])
data class City(@PrimaryKey var id: Int = 0,
                var provinceId: Int = 0,
                var provinceName: String? = null,
                var name: String? = null) : Parcelable


object CityPickerConverter {
    /**
     * Note: 阻塞当前线程
     */
    @JvmStatic
    fun convert(cities: List<City>): List<com.zaaach.citypicker.model.City> {
        val results = ArrayList<com.zaaach.citypicker.model.City>()
        runBlocking {
            withContext(Dispatchers.Default) {
                for (city in cities) {
                    val pc = com.zaaach.citypicker.model.City(city.name, city.provinceName, Pinyin.toPinyin(city.name, ""), city.id.toString())
                    pc.provinceCode = city.provinceId.toString()
                    results.add(pc)
                }
                results.sortBy {
                    it.pinyin
                }
            }
        }
        return results
    }

    /**
     * Note: 阻塞当前线程
     */
    fun reverseConvert(cities: List<com.zaaach.citypicker.model.City>): List<City> {
        val results = ArrayList<City>()
        runBlocking {
            withContext(Dispatchers.Default) {
                for (city in cities) {
                    val pc = City(city.code.toInt(), city.provinceCode.toInt(), city.province, city.name)
                    results.add(pc)
                }
            }
        }
        return results
    }
}

