package com.snt.phoney.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
@Entity(foreignKeys = [
    ForeignKey(entity = Province::class,
            parentColumns = ["id"],
            childColumns = ["provinceId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
    )
], tableName = "City")
data class City(@PrimaryKey var id: Int = 0,
                var provinceId: Int = 0,
                @Ignore var provinceName: String? = null,
                var name: String? = null)


object CityPickerConvertor {
    @JvmStatic
    fun convert(cities: List<City>): List<com.zaaach.citypicker.model.City> {
        val results = ArrayList<com.zaaach.citypicker.model.City>()
        runBlocking {
            async {
                for ((index, city) in cities.withIndex()) {
                    val pc = com.zaaach.citypicker.model.City(city.name, city.provinceName, Pinyin.toPinyin(city.name, ""), index.toString())
                    results.add(pc)
                }
            }.await()
        }
        return results
    }
}

