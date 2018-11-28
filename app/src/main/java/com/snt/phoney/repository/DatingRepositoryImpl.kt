package com.snt.phoney.repository

import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.DatingRepository
import io.reactivex.Single
import javax.inject.Inject

class DatingRepositoryImpl @Inject constructor(private val api: Api) : DatingRepository {
    override fun follow(token: String, uuid: String): Single<Response<Boolean>> {
        return api.follow(token, uuid)
    }

    override fun publishDating(token: String, content: String, city: String, days: String, latitude: Double, longitude: Double, title: String, location: String, program: String): Single<Response<List<User>>> {
        return api.publishDating(token, content, city, days, latitude.toString(), longitude.toString(), title, location, program)
    }

    override fun cancelDating(token: String, uuid: String): Single<Response<String>> {
        return api.cancelDating(token, uuid)
    }

    override fun joinDating(token: String, uuid: String): Single<Response<String>> {
        return api.joinDating(token, uuid)
    }

    override fun viewDating(token: String, uuid: String): Single<Response<String>> {
        return api.viewDating(token, uuid)
    }

    override fun reviewDating(token: String, uuid: String, state: String): Single<Response<String>> {
        return api.reviewDating(token, uuid, state)
    }

    override fun listDatingProgram(token: String, uuid: String): Single<Response<List<DatingProgram>>> {
        return api.listDatingProgram(token, uuid)
    }

    override fun listDatingByUser(token: String, uuid: String, pageIndex: Int): Single<Response<List<Dating>>> {
        return api.listDatingByUser(token, uuid, pageIndex.toString())
    }

    override fun listRecommendDating(token: String, pageIndex: Int, dateType: Int, distanceType: Int, program: String, latitude: Double, longitude: Double): Single<Response<List<Dating>>> {
        return api.listRecommendDating(token, pageIndex.toString(), dateType, distanceType, program, latitude.toString(), longitude.toString())
    }

    override fun listPopularDating(token: String, pageIndex: Int): Single<Response<List<Dating>>> {
        return api.listPopularDating(token, pageIndex.toString())
    }

    override fun getDatingDetail(token: String, uuid: String, latitude: Double, longitude: Double): Single<Response<List<Dating>>> {
        return api.getDatingDetail(token, uuid, latitude.toString(), longitude.toString())
    }

    override fun listJoinedDating(token: String, pageIndex: Int): Single<Response<List<Dating>>> {
        return api.listJoinedDating(token, pageIndex.toString())
    }

    override fun listAppliedDating(token: String, pageIndex: Int): Single<Response<List<Dating>>> {
        return api.listAppliedDating(token, pageIndex.toString())
    }

}