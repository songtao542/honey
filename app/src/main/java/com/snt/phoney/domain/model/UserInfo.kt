package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class UserInfo(
        @SerializedName(value = "count_visitor") var countVisitor: Int = 0,
        @SerializedName(value = "count_care") var countFollowed: Int = 0,
        @SerializedName(value = "count_appointment") var countDating: Int = 0,
        @SerializedName(value = "auth")  var authState: AuthState?=null,//"auth": {
        @SerializedName(value = "message_appointment") var hasNewsOfDating:Boolean = false,//  "message_appointment": false,
        @SerializedName(value = "message_wallet") var hasNewsOfWallet:Boolean = false,//"message_wallet": false,
        @SerializedName(value = "member") var memberInfo: MemberInfo?=null//"member": {
)




