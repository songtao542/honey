package com.snt.phoney.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity(tableName = "Photo")
@Parcelize
@Serializable
@TypeConverters(value = [Converter::class])
data class Photo(
        @PrimaryKey var id: Long = 0,      // "id": 44
        var path: String? = null, //   "path": "http://phoney.alance.pub/phoney/phoney/512/users/photos/u201811081829582566215b5a5a3b4f/1542507758452-5-a61dd1050281015775b699c828a394bc.jpg",
        /**
         * 0 正常 ， 1 是红包
         */
        var flag: Int = 0, //0 正常 ， 1 是红包
        @SerializedName(value = "money") var price: Int = 0,
        var burn: Int = -1,//  是否已焚，0 未焚，1 已焚
        var paid: Boolean = false,//红包的时候，已支付1，未支付0
        /**
         * burnTime, ownerId 在初始化用户信息时进行填充, 服务端不下发这两个字段, 详见 User 类的 init
         */
        var burnTime: Int = 0,
        var ownerId: String? = null //该相片所有者的 uuid,
) : Selectable(), Parcelable {

    @Transient
    val burnTimeInMillis: Long
        get() {
            return if (burnTime <= 0) 5000 else burnTime.toLong() * 1000
        }

}


//权限值(0 全部公开 1 红包（只有单张） 2 需要解锁相册(全部) 3 需要申请查看 4 不可查看)
enum class PhotoPermission(val value: Int) {
    PUBLIC(0), //全部公开
    NEED_CHARGE(1), //红包（只有单张）
    LOCKED(2), //需要解锁相册(全部)
    NEED_APPLY(3), // 需要申请查看
    PRIVATE(4); //不可查看

    companion object {
        fun from(value: Int): PhotoPermission {
            return when (value) {
                0 -> PUBLIC
                1 -> NEED_CHARGE
                2 -> LOCKED
                3 -> NEED_APPLY
                else -> PRIVATE
            }
        }
    }
}