package com.snt.phoney.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.*
import kotlinx.serialization.json.JSON


@Entity(tableName = "User")
@Parcelize
@Serializable
@TypeConverters(value = [Converter::class])
data class User(
        @PrimaryKey var id: String = "",
        var uuid: String? = null,
        var username: String? = null,
        @SerializedName(value = "nickName") var nickname: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var password: String? = null,
        @Optional var avatar: String? = null,
        var sex: Int = -1,
        var height: Int = 0,
        var weight: Double = 0.0,
        var age: Int = 0,
        var cup: String? = null,
        var city: String? = null,
        var cities: List<City>? = null,
        var career: String? = null,
        var portrait: String? = null,
        var introduce: String? = null,
        var photo: String? = null,
        var photoRight: String? = null,
        @SerializedName(value = "account_wx") var wechatAccount: String? = null,
        @SerializedName(value = "pauthentication") var verified: String? = null,
        var token: String? = null,
        var open: Int = 0,
        var price: Float = 0f,
        var state: Int = 0,
        var tag: String? = null,
        @SerializedName(value = "cares") var followedSize: Int = 0,
        @SerializedName(value = "utime") var updateTime: Long = 0) : Parcelable {

    @Transient
    val safeCup: String
        get() = cup ?: ""

    @Transient
    val safeNickname: String
        get() = nickname ?: ""

    @Transient
    val safeIntroduce: String
        get() = introduce ?: ""

    @Transient
    val safeCareer: String
        get() = career ?: ""

    @Transient
    val safeWechatAccount: String
        get() = wechatAccount ?: ""

    @Transient
    val cityCodesString: String
        get() {
            return cities?.map { it.id }?.joinToString(separator = ",") ?: ""
        }

    @Transient
    val cityNamesString: String
        get() {
            return cities?.map { it.name }?.joinToString(separator = " ") ?: city ?: ""
        }
}

enum class Sex(val value: Int) {
    MALE(0),
    FEMALE(1),
    UNKNOWN(-1);

    companion object {
        fun from(value: Int): Sex {
            return when (value) {
                0 -> MALE
                1 -> FEMALE
                else -> UNKNOWN
            }
        }
    }
}


class Converter {

    @TypeConverter
    fun toCityList(value: String): List<City> {
        return JSON.nonstrict.parse(City.serializer().list, value)
    }

    @TypeConverter
    fun cityListToJson(list: List<City>): String {
        return JSON.nonstrict.stringify(City.serializer().list, list)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return JSON.nonstrict.parse(String.serializer().list, value)
    }

    @TypeConverter
    fun stringListToJson(list: List<String>): String {
        return JSON.nonstrict.stringify(String.serializer().list, list)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        //val listType = object : TypeToken<ArrayList<Int>>() {}.type
        //return Gson().fromJson(value, listType)
        return JSON.nonstrict.parse(Int.serializer().list, value)
    }

    @TypeConverter
    fun intListToJson(list: List<Int>): String {
        //return Gson().toJson(list)
        return JSON.nonstrict.stringify(Int.serializer().list, list)
    }
}