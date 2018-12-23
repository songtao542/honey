package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject


class GetUserInfoUseCase @Inject constructor(private val userRepository: UserRepository, private val locationRepository: LocationRepository) : AccessUserUseCase(userRepository) {

    fun getUserAmountInfo(token: String) = userRepository.getUserAmountInfo(token)

    fun getUserInfo(token: String, uid: String, latitude: Double, longitude: Double) = userRepository.getUserInfo(token, uid, latitude, longitude)

    fun getLocation() = locationRepository.getLocation()

    fun setPhotoPermission(token: String,
                           photoPermission: Int,
                           money: Double,
                           photoId: String) = userRepository.setPhotoPermission(token, photoPermission, money, photoId)

    fun uploadPhotos(token: String, photos: List<File>) = userRepository.uploadPhotos(token, photos)

    fun getUserPhotos(token: String) = userRepository.getUserPhotos(token)

    fun deletePhotos(token: String, photoIds: List<String>) = userRepository.deletePhotos(token, photoIds)

    fun deleteUser(token: String) = userRepository.deleteUser(token)

}