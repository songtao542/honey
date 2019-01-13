package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.ToolRepository
import com.snt.phoney.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject


class UserInfoUseCase @Inject constructor(private val userRepository: UserRepository,
                                          private val locationRepository: LocationRepository,
                                          private val toolRepository: ToolRepository) : AccessUserUseCase(userRepository) {

    fun getAllInfoOfUser(token: String) = userRepository.getAllInfoOfUser(token)

    fun getUserInfo(token: String, uid: String, latitude: Double, longitude: Double) = userRepository.getUserInfo(token, uid, latitude, longitude)

    fun setWalletNewsToRead(token: String) = userRepository.setWalletNewsToRead(token)

    fun getLocation() = locationRepository.getLocation()

    fun setPhotoPermission(token: String,
                           photoPermission: Int,
                           money: Double,
                           photoId: String) = userRepository.setPhotoPermission(token, photoPermission, money, photoId)

    fun uploadPhotos(token: String, photos: List<File>) = userRepository.uploadPhotos(token, photos)
    fun uploadHeadIcon(token: String, file: File) = userRepository.uploadHeadIcon(token, file)

    fun getUserPhotos(token: String) = userRepository.getUserPhotos(token)
    fun getUserWechatAccount(token: String, uid: String) = userRepository.getUserWechatAccount(token, uid)

    fun applyToViewPhotos(token: String, target: String) = userRepository.applyToViewPhotos(token, target)

    fun reviewPhotoApply(token: String, uuid: String, state: Int) = userRepository.reviewPhotoApply(token, uuid, state)

    fun listPhotoApply(token: String, page: Int) = userRepository.listPhotoApply(token, page)

    fun deletePhotos(token: String, photoIds: List<String>) = userRepository.deletePhotos(token, photoIds)

    fun follow(token: String, uuid: String) = userRepository.follow(token, uuid)

    fun deleteUser(token: String) = userRepository.deleteUser(token)

    fun testSignGet(token: String, page: String) = toolRepository.testSignGet(token, page)
    fun testSignPost(token: String, page: String) = toolRepository.testSignPost(token, page)

}