package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserCredentialRepository
import javax.inject.Inject

class SetupWizardUseCase @Inject constructor(private val locationRepository: LocationRepository, private val userRepository: UserCredentialRepository) {

    fun getCities() = locationRepository.cities

    fun setUserSex(token: String, sex: Int) = userRepository.setUserSex(token, sex)

    fun setUserFeatures(token: String,
                        height: Int,
                        weight: Int,
                        age: Int,
                        cup: String) = userRepository.setUserFeatures(token, height, weight, age, cup)

    var user: User?
        set(value) {
            userRepository.user = value
        }
        get() {
            return userRepository.user
        }
}