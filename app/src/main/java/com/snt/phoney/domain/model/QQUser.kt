package com.snt.phoney.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class QQUser(val openId: String,
                  val thirdToken: String,
                  val plate: String,
                  val nickName: String,
                  val headPic: String,
                  val sex: Int,
                  val province: String,
                  val city: String,
                  val year: Int)