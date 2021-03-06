package com.snt.phoney.domain.model

import android.os.Parcelable
import android.text.TextUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import kotlinx.serialization.serializer


@Entity(tableName = "User")
@Parcelize
@Serializable
@TypeConverters(value = [Converter::class])
data class User(
        @PrimaryKey var id: Long = 0,
        @SerializedName(value = "uuid", alternate = ["uid"]) var uuid: String? = null,
        /**
         * 用于JPush alias
         */
        var deviceToken: String? = null,
        @SerializedName(value = "userName") var username: String? = null,
        @SerializedName(value = "nickName") var nickname: String? = null,
        var email: String? = null,
        /**
         * 隐私密码，正序 md5
         */
        var password: String? = null,
        /**
         * 隐私密码，反序 md5
         */
        @SerializedName(value = "privatePassword") var privacyPassword: String? = null,
        var phone: String? = null,
        var sex: Int = -1,
        var height: Int = 0,
        var weight: Double = 0.0,
        var age: Int = 0,
        var cup: String? = null,
        var purpose: String? = null,
        var city: String? = null,
        @SerializedName(value = "citys", alternate = ["cities"]) var cities: List<City>? = null,
        var career: String? = null,
        @SerializedName(value = "portrait") var avatar: String? = null,//肖像
        var introduce: String? = null,
        @SerializedName(value = "photo") var photos: List<Photo>? = null,
        @SerializedName(value = "photoRight") var photoPermission: Int = 0,
        /**
         * -1未申请, 0 审核中, 1 通过, 2 被拒绝
         */
        var photoApplyStatus: Int = -1,
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
        @SerializedName(value = "isMember") var member: Int = 0,
        @SerializedName(value = "endTime") var memberEndTime: Long = 0,
        var token: String? = null,
        @SerializedName(value = "isOpen") var open: Int = 0,
        var price: Double = 0.0,
        /**
         * 0 未认证  2 已认证
         */
        var state: Int = 0,
        var tag: String? = null,
        var distance: Double = 0.0,
        var program: String? = null,
        var im: ImUser? = null,
        @SerializedName(value = "isCare") var isCared: Boolean = false,
        @SerializedName(value = "cares") var caredSize: Int = 0,
        @SerializedName(value = "utime") var updateTime: Long = 0,
        @SerializedName(value = "burn_time") var burnTime: Int = 0) : Parcelable {

    fun fixPhotos() {
        photos?.let { ps ->
            for (photo in ps) {
                photo.ownerId = uuid
                photo.burnTime = burnTime
            }
        }
    }

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
                //program 返回的是 交友目的
                1 -> height > 0 && age > 0 && cup != null && cities?.isNotEmpty() ?: false && career != null && program != null
                0 -> height > 0 && age > 0 && weight > 0 && cities?.isNotEmpty() ?: false && career != null && program != null
                else -> false
            }
        }

    @Transient
    val isSexValid: Boolean
        get() {
            return sex == 0 || sex == 1
        }

    @Transient
    val verified: Boolean
        get() = state == 2

    @Transient
    val freePhotos: List<Photo>?
        get() {
            fixPhotos()
            photos?.let { photos ->
                val result = ArrayList<Photo>()
                for (photo in photos) {
                    if (photo.flag == 0 //普通情况，不需要红包，也不需要解锁
                            || (photo.flag == 1 && photo.paid) //需要红包，但是已经支付了
                            || (photo.flag == -1 && !TextUtils.isEmpty(photo.path)) //服务端未返回 flag(默认值-1) 字段，但是 path 又不为空，表示需要解锁，但是已经解锁
                    ) {
                        result.add(photo)
                    }
                }
                return result
            }
            return null
        }

    @Transient
    val isPhotoNeedUnlock: Boolean
        get() {
            val needUnlock = photoPermission == PhotoPermission.LOCKED.value
            return needUnlock && !isPhotoFree
        }

    @Transient
    val isPhotoNeedApply: Boolean
        get() {
            return photoPermission == PhotoPermission.NEED_APPLY.value && photoApplyStatus != 1
        }

    @Transient
    val isPrivacyPasswordOpen: Boolean
        get() {
            return open == 1 && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(privacyPassword)
        }

    fun updatePrivacyPassword(password: String?, privacyPassword: String?): User {
        this.open = if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(privacyPassword)) 1 else 0
        this.password = password
        this.privacyPassword = privacyPassword
        return this
    }

    @Transient
    val isMember: Boolean
        get() {
            return member == 1
        }

    @Transient
    val isValidMember: Boolean
        get() {
            return member == 1 && memberEndTime > System.currentTimeMillis()
        }

    fun updateMemberInfo(memberInfo: MemberInfo?): User {
        memberInfo?.let {
            member = if (it.isMember) 1 else 0
            memberEndTime = it.endTime
        }
        return this
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
    fun toImInfo(value: String): ImUser {
        return JSON.nonstrict.parse(ImUser.serializer(), value)
    }

    @TypeConverter
    fun imInfoToJson(info: ImUser): String {
        return JSON.nonstrict.stringify(ImUser.serializer(), info)
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