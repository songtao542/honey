package com.snt.phoney.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Photo(
        var id: Int = 0,      // "id": 44
        var path: String? = null, //   "path": "http://phoney.alance.pub/phoney/phoney/512/users/photos/u201811081829582566215b5a5a3b4f/1542507758452-5-a61dd1050281015775b699c828a394bc.jpg",
        var flag: Int = 0     //  "flag": 0,
) : Parcelable


//权限值(0 全部公开 1 红包（只有单张） 2 解锁相册(全部) 3 需要申请查看 4 不可查看)
enum class PhotoPermission(val value: Int) {
    PUBLIC(0),
    NEED_CHARGE(1),
    UNLOCKED(2),
    NEED_APPLY(3),
    PRIVATE(4);

    companion object {
        fun from(value: Int): PhotoPermission {
            return when (value) {
                0 -> PUBLIC
                1 -> NEED_CHARGE
                2 -> UNLOCKED
                3 -> NEED_APPLY
                else -> PRIVATE
            }
        }
    }
}