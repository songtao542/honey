package com.snt.phoney.domain.model

import androidx.room.*
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.coroutines.Dispatchers
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
], tableName = "City", indices = [Index(value = ["provinceId"], unique = true)])
data class City(@PrimaryKey var id: Int = 0,
                var provinceId: Int = 0,
                @Ignore var provinceName: String? = null,
                var name: String? = null)


object CityPickerConverter {
    @JvmStatic
    fun convert(cities: List<City>): List<com.zaaach.citypicker.model.City> {
        val results = ArrayList<com.zaaach.citypicker.model.City>()
        runBlocking {
            async(Dispatchers.Default) {
                for (city in cities) {
                    val pc = com.zaaach.citypicker.model.City(city.name, city.provinceName, Pinyin.toPinyin(city.name, ""), city.id.toString())
                    results.add(pc)
                }
                results.sortBy {
                    it.pinyin
                }
            }.await()
        }
        return results
    }
}

