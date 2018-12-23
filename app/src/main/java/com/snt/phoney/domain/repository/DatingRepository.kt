package com.snt.phoney.domain.repository

import com.snt.phoney.domain.model.*
import io.reactivex.Single
import java.io.File

interface DatingRepository {

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
    ): Single<Response<String>>

    /**
     * 取消约会
     */
    fun cancelDating(token: String, uuid: String): Single<Response<String>>

    /**
     * 参加约会
     */
    fun joinDating(token: String, uuid: String): Single<Response<String>>

    fun quitDating(token: String, uuid: String): Single<Response<String>>

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

    fun listMyDating(token: String, pageIndex: Int): Single<Response<List<Dating>>>

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
                        longitude: Double): Single<Response<Dating>>

    /**
     * 参加的约会
     */
    fun listJoinedDating(token: String, pageIndex: Int): Single<Response<List<Dating>>>

    /**
     * 被申请的约会
     */
    fun listDatingApplicant(token: String, uuid: String, pageIndex: Int): Single<Response<List<Applicant>>>

    /**
     * 被申请的约会
     */
    fun listApplicant(token: String, pageIndex: Int): Single<Response<List<Applicant>>>

    fun follow(token: String, uuid: String): Single<Response<Boolean>>
}