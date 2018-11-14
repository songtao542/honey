package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.Province
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserCredentialRepository
import io.reactivex.Single
import javax.inject.Inject

class SignupUseCase @Inject constructor(private val locationRepository: LocationRepository, private val userRepository: UserCredentialRepository) {
    fun getCities(): Single<Response<List<Province>>> = locationRepository.getCities()

    fun signupByThirdPlatform(openId: String, //第三方openid（qq是uid）
                              thirdToken: String,
                              plate: String,
                              nickName: String,
                              headPic: String,
                              deviceToken: String,
                              osVersion: String,
                              version: String,
                              mobilePlate: String) = userRepository.signupByThirdPlatform(openId, thirdToken, plate, nickName, headPic, deviceToken, osVersion, version, mobilePlate)
}