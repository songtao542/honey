package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class ResetPrivacyPasswordUseCase @Inject constructor(private val userRepository: UserRepository) : AccessUserUseCase(userRepository) {
    fun getResetPasswordState(token: String) = userRepository.getResetPasswordState(token)

    fun uploadResetPasswordFile(token: String, file: File) = userRepository.uploadResetPasswordFile(token, file)

    fun cancelResetPassword(token: String) = userRepository.cancelResetPassword(token)

    fun resetPassword(token: String, password: String, privatePassword: String) = userRepository.resetPassword(token, password, privatePassword)

}