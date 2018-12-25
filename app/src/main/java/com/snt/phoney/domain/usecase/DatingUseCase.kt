package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.DatingRepository
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class DatingUseCase @Inject constructor(private val repository: DatingRepository,
                                        private val userRepository: UserRepository,
                                        private val locationRepository: LocationRepository) : AccessUserUseCase(userRepository) {

    fun getDatingDetail(token: String, uuid: String, latitude: Double, longitude: Double) = repository.getDatingDetail(token, uuid, latitude, longitude)

    fun getLocation() = locationRepository.getLocation()

    fun joinDating(token: String, uuid: String) = repository.joinDating(token, uuid)
    fun quitDating(token: String, uuid: String) = repository.quitDating(token, uuid)

    fun listDatingByUser(token: String, uuid: String, pageIndex: Int) = repository.listDatingByUser(token, uuid, pageIndex)

    fun listMyDating(token: String, pageIndex: Int) = repository.listMyDating(token, pageIndex)

    fun listDatingApplicant(token: String, uuid: String, pageIndex: Int) = repository.listDatingApplicant(token, uuid, pageIndex)

    fun listApplicant(token: String, pageIndex: Int) = repository.listApplicant(token, pageIndex)

    fun reviewDating(token: String, uuid: String, state: String) = repository.reviewDating(token, uuid, state)

    fun listJoinedDating(token: String, pageIndex: Int) = repository.listJoinedDating(token, pageIndex)


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
                      cover: List<File>) = repository.publishDating(token, title, program, content, days, city, location, latitude, longitude, cover)

}