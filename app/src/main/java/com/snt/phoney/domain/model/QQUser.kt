package com.snt.phoney.domain.model

data class QQUser(val openId: String,
                  val thirdToken: String,
                  val plate: String,
                  val nickName: String,
                  val headPic: String,
                  val sex: Int,
                  val province: String,
                  val city: String,
                  val year: Int)