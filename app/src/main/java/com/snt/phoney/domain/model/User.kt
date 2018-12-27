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
        @PrimaryKey var id: Int = 0,
        var uuid: String? = null,
        @SerializedName(value = "userName") var username: String? = null,
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
        var purpose: String? = null,
        var city: String? = null,
        var cities: List<City>? = null,
        var career: String? = null,
        var portrait: String? = null,//肖像
        var introduce: String? = null,
        @SerializedName(value = "photo") var photos: List<Photo>? = null,
        @SerializedName(value = "photoRight") var photoPermission: Int = 0,
        /**
         * 相册价格，当相册权限为 {@link PhotoPermission#LOCKED} 时，需要解锁相册的价格
         */
        @SerializedName(value = "photoAllPrice") var photoPrice: Int = 0,
        @SerializedName(value = "photo_id") var photoId: Int = 0,
        /**
         * 标识是否已经支付了相册蜜币
         */
        @SerializedName(value = "isPhotoFree") var isPhotoFree: Boolean = true,
        @SerializedName(value = "hasWX") var hasWechatAccount: Boolean = false,
        @SerializedName(value = "account_wx") var wechatAccount: String? = null,
        /**
         * pauthentication 认证状态
         */
        @SerializedName(value = "pauthentication") var verified: Int = 0,
        var token: String? = null,
        @SerializedName(value = "isOpen") var open: Int = 0,
        var price: Double = 0.0,
        var state: Int = 0,
        var tag: String? = null,
        var distance: Double = 0.0,
        var program: String? = null,
        var im: ImInfo? = null,
        @SerializedName(value = "isCare") var care: Boolean = false,
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
    val safeUuid: String
        get() = uuid ?: ""

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

    @Transient
    val validated: Boolean
        get() {
            return when (sex) {
                1 -> height > 0 && age > 0 && cup != null && cities?.isNotEmpty() ?: false && career != null && purpose != null
                0 -> height > 0 && age > 0 && weight > 0 && cities?.isNotEmpty() ?: false && career != null && purpose != null
                else -> false
            }
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
    fun toImInfo(value: String): ImInfo {
        return JSON.nonstrict.parse(ImInfo.serializer(), value)
    }

    @TypeConverter
    fun imInfoToJson(info: ImInfo): String {
        return JSON.nonstrict.stringify(ImInfo.serializer(), info)
    }

    @TypeConverter
    fun toPhotoList(value: String): List<Photo> {
        return JSON.nonstrict.parse(Photo.serializer().list, value)
    }

    @TypeConverter
    fun photoListToJson(list: List<Photo>): String {
        return JSON.nonstrict.stringify(Photo.serializer().list, list)
    }

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