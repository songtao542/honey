package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.DatingRepository
import com.snt.phoney.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class CreateDatingUseCase @Inject constructor(private val repository: DatingRepository, private val userRepository: UserRepository) : AccessUserUseCase(userRepository)  {

    fun listDatingProgram(token: String, uuid: String) = repository.listDatingProgram(token, uuid)

    fun publishDating(token: String,
                      title: String,
                      program: String,
                      content: String,
                      days: Int,
                      city: String,
                      location: String,
                      latitude: Double,
                      longitude: Double,
                      cover: List<File>
    ) = repository.publishDating(token, title, program, content, days, city, location, latitude, longitude, cover)


}