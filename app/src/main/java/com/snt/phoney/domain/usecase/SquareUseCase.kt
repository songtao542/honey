package com.snt.phoney.domain.usecase

import android.location.Location
import com.snt.phoney.domain.repository.DatingRepository
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

class SquareUseCase @Inject constructor(private val repository: DatingRepository, private val userRepository: UserRepository, private val locationRepository: LocationRepository): AccessUserUseCase(userRepository)  {

    /**
     * 推荐约会
     */
    fun listRecommendDating(token: String,
                            pageIndex: Int,
                            dateType: String,
                            distanceType: String,
                            program: String,
                            latitude: Double,
                            longitude: Double) = repository.listRecommendDating(token, pageIndex, dateType, distanceType, program, latitude, longitude)

    /**
     * 热门约会
     */
    fun listPopularDating(token: String, pageIndex: Int) = repository.listPopularDating(token, pageIndex)

    fun joinDating(token: String, uuid: String) = repository.joinDating(token, uuid)
    fun follow(token: String, uuid: String) = repository.follow(token, uuid)

    var location: Observable<Location> = locationRepository.getLocation()

}