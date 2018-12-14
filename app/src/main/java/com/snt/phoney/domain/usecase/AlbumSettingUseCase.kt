package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class AlbumSettingUseCase @Inject constructor(val repository: UserRepository) {
    fun setPhotoPermission(token: String,
                           photoPermission: Int,
                           money: Double,
                           photoId: String) = repository.setPhotoPermission(token, photoPermission, money, photoId)

    fun getUserPhotoes(token: String) = repository.getUserPhotos(token)

    var user: User?
        get() = repository.user
        set(value) {
            repository.user = value
        }
}