package com.snt.phoney.domain.repository

import com.snt.phoney.domain.model.Dating
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import io.reactivex.Single

interface DatingRepository {

    fun publishDating(token: String,
                      content: String,
                      city: String,
                      days: String,
                      latitude: Double,
                      longitude: Double,
                      title: String,
                      location: String,
                      program: String): Single<Response<List<User>>>

    /**
     * 取消约会
     */
    fun cancelDating(token: String, uuid: String): Single<Response<String>>

    /**
     * 参加约会
     */
    fun joinDating(token: String, uuid: String): Single<Response<String>>

    /**
     * 查看约会
     */
    fun viewDating(token: String, uuid: String): Single<Response<String>>

    /**
     * 审核约会
     */
    fun reviewDating(token: String, uuid: String, state: String): Single<Response<String>>

    /**
     * 约会节目
     */
    fun listDatingProgram(token: String, uuid: String): Single<Response<List<DatingProgram>>>

    /**
     * 该用户发布的约会
     */
    fun listDatingByUser(token: String,
                         uuid: String,
                         pageIndex: Int): Single<Response<List<Dating>>>

    /**
     * 推荐约会
     */
    fun listRecommendDating(token: String,
                            pageIndex: Int,
                            dateType: Int,
                            distanceType: Int,
                            program: String,
                            latitude: Double,
                            longitude: Double): Single<Response<List<Dating>>>

    /**
     * 热门约会
     */
    fun listPopularDating(token: String, pageIndex: Int): Single<Response<List<Dating>>>

    /**
     * 约会详情
     */
    fun getDatingDetail(token: String,
                        uuid: String,
                        latitude: Double,
                        longitude: Double): Single<Response<List<Dating>>>

    /**
     * 参加的约会
     */
    fun listJoinedDating(token: String, pageIndex: Int): Single<Response<List<Dating>>>

    /**
     * 被申请的约会
     */
    fun listAppliedDating(token: String, pageIndex: Int): Single<Response<List<Dating>>>

    fun follow(token: String, uuid: String): Single<Response<Boolean>>
}