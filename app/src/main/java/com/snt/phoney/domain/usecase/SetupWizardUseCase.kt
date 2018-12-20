package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.ToolRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class SetupWizardUseCase @Inject constructor(private val locationRepository: LocationRepository,
                                             private val userRepository: UserRepository,
                                             private val toolRepository: ToolRepository) : AccessUserUseCase(userRepository) {

    fun getCities() = locationRepository.cities

    fun setUserSex(token: String, sex: Int) = userRepository.setUserSex(token, sex)

    fun setUserFeatures(token: String,
                        height: Int,
                        weight: Int,
                        age: Int,
                        cup: String) = userRepository.setUserFeatures(token, height, weight, age, cup)

    fun listCareer(token: String) = toolRepository.listCareer(token)

    fun listPurpose(token: String) = toolRepository.listPurpose(token)

    fun setUserInfo(token: String, cities: String, career: String, program: String) = userRepository.setUserInfo(token, cities, career, program)

}