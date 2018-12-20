package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class AlbumSettingUseCase @Inject constructor(val repository: UserRepository) : AccessUserUseCase(repository) {
    fun setPhotoPermission(token: String,
                           photoPermission: Int,
                           money: Double,
                           photoId: String) = repository.setPhotoPermission(token, photoPermission, money, photoId)

    fun getUserPhotos(token: String) = repository.getUserPhotos(token)

}