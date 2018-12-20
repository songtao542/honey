package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

const val PLATFORM_QQ = "0"
const val PLATFORM_WECHAT = "1"
const val PLATFORM_WEIBO = "3"

class SigninUseCase @Inject constructor(private val repository: UserRepository): AccessUserUseCase(repository)  {
    fun signup(phone: String, msgId: String, code: String, deviceToken: String,
               osVersion: String, version: String, mobilePlate: String) =
            repository.signup(phone, msgId, code, deviceToken, osVersion, version, mobilePlate)


    fun signupByThirdPlatform(openId: String, //第三方openid（qq是uid）
                              thirdToken: String,
                              plate: String,
                              nickName: String,
                              headPic: String,
                              deviceToken: String,
                              osVersion: String,
                              version: String,
                              mobilePlate: String) = repository.signupByThirdPlatform(openId, thirdToken, plate, nickName, headPic, deviceToken, osVersion, version, mobilePlate)


    fun requestVerificationCode(phone: String): Single<Response<String>> = repository.requestVerificationCode(phone)

}