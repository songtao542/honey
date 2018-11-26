package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
        var id: Int,//"id": 14,
        var uuid: String,//"uuid": "u201811081829582566215b5a5a3b4f",
        var career: String,//    "career": "自由职业",
        var cities: List<City>,//"cities": [
        var distance: Double,// "distance": 0.4,
        var city: String,//"city": "深圳市",
        @SerializedName(value = "nickName") var nickname: String,//"nickName": "天涯若比邻",
        var introduce: String,//"introduce": "我才不会",
        var sex: Int = -1,//"sex": 0,
        var photoRight: Int,// "photoRight": 1,
        var weight: Int,//"weight": 54,
        @SerializedName(value = "photo") var photos: List<PhotoInfo>,//"photo": [
        var program: String,//  "program": null,
        var portrait: String,//"portrait": "http://phoney.alance.pub/phoney/phoney/512/users/portrait/1542637068739-0-70e533da3ea611c4551af38a9bf691f5.jpg",
        var price: Double,//"price": null,
        @SerializedName(value = "isCare") var care: Boolean,//"isCare": false,
        var state: Int,//"state": 0,
        @SerializedName(value = "hasWX") var hasWechat: Boolean,//"hasWX": true,
        var age: Int,//"age": 24,
        var height: Int,//"height": 175,
        var cup: String//"cup": null
)


data class PhotoInfo(
        var id: Int,      // "id": 44
        var path: String, //   "path": "http://phoney.alance.pub/phoney/phoney/512/users/photos/u201811081829582566215b5a5a3b4f/1542507758452-5-a61dd1050281015775b699c828a394bc.jpg",
        var flag: Int     //  "flag": 0,
)