package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class EditUserUseCase @Inject constructor(private val repository: UserRepository, private val locationRepository: LocationRepository) : AccessUserUseCase(repository) {

    /**
     * Note: 阻塞当前线程
     */
    fun getCities() = locationRepository.cities

    fun setFullUserInfo(token: String,
                        height: Int,
                        weight: Double,
                        age: Int,
                        cup: String,
                        cities: String,
                        introduce: String,
                        career: String,
                        program: String,
                        wechatAccount: String,
                        nickname: String) = repository.setFullUserInfo(token, height, weight, age, cup, cities, introduce, career, program, wechatAccount, nickname)
}