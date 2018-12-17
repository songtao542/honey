package com.snt.phoney.repository


import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.AmountInfo
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.domain.repository.UserRepository
import com.snt.phoney.utils.data.Constants
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val cache: CacheRepository, private val api: Api) : UserRepository {
    override fun setPhotoPermission(token: String, photoPermission: Int, money: Double, photoId: String): Single<Response<String>> {
        return api.setPhotoPermission(token, photoPermission.toString(), money.toString(), photoId)
    }

    override fun getUserPhotos(token: String): Single<Response<List<Photo>>> {
        return api.getUserPhotos(token)
    }

    override fun listVisitor(token: String): Single<Response<List<User>>> {
        return api.listVisitor(token)
    }

    override fun getUserAmountInfo(token: String): Single<Response<AmountInfo>> {
        return api.getUserAmountInfo(token)
    }

    override fun follow(token: String, uuid: String): Single<Response<Boolean>> {
        return api.follow(token, uuid)
    }

    override fun listFollow(token: String): Single<Response<List<User>>> {
        return api.listFollow(token)
    }

    override fun setFullUserInfo(token: String, height: Int, weight: Double, age: Int, cup: String, cities: String, introduce: String, career: String, program: String, wechatAccount: String, nickname: String): Single<Response<String>> {
        return api.setFullUserInfo(token, height.toString(), weight.toInt().toString(), age.toString(), cup, cities, introduce, career, program, wechatAccount, nickname)
    }

    override fun getUserInfo(token: String, uid: String, latitude: Double, longitude: Double): Single<Response<User>> {
        return api.getUserInfo(token, uid, latitude.toString(), longitude.toString())
    }

    override fun listUser(token: String, latitude: String, longitude: String, type: String, page: String, city: String,
                          heightStart: String, heightEnd: String,
                          ageStart: String, ageEnd: String,
                          cupStart: String, cupEnd: String): Single<Response<List<User>>> {
        return api.listUser(token, latitude, longitude, type, page, city, heightStart, heightEnd, ageStart, ageEnd, cupStart, cupEnd)
    }

    override fun setUserInfo(token: String, cities: String, career: String, program: String): Single<Response<String>> {
        return api.setUserInfo(token, cities, career, program)
    }


    override fun bindPhone(msgId: String, code: String, phone: String, uuid: String, token: String): Single<Response<String>> {
        return api.bindPhone(msgId, code, phone, uuid, token)
    }

    override fun requestVerificationCode(phone: String): Single<Response<String>> {
        return api.sendMsg(phone)
    }

    override fun signup(phone: String, msgId: String, code: String, deviceToken: String, osVersion: String, version: String, mobilePlate: String): Single<Response<User>> {
        return api.signupByMsgCode(phone, msgId, code, deviceToken, osVersion, version, mobilePlate)
    }

    override fun signupByThirdPlatform(openId: String, thirdToken: String, plate: String, nickName: String, headPic: String, deviceToken: String, osVersion: String, version: String, mobilePlate: String): Single<Response<User>> {
        return api.signupByThirdPlatform(openId, thirdToken, plate, nickName, headPic, deviceToken, osVersion, version, mobilePlate)
    }

    override fun setUserSex(token: String, sex: Int): Single<Response<String>> {
        return api.setUserSex(token, sex)
    }

    override fun setUserFeatures(token: String,
                                 height: Int,
                                 weight: Int,
                                 age: Int,
                                 cup: String): Single<Response<String>> {
        return api.setUserFeatures(token, height.toString(), weight.toString(), age.toString(), cup)
    }


//    override fun login(username: String, password: String): LiveData<Response<User>> {
//        return api.login(username, password)
//    }
//    override fun resetPassword(username: String, password: String): LiveData<Response<String>> {
//        return api.resetPassword(username, password)
//    }
//    override fun logout(username: String): LiveData<Response<String>> {
//        return api.logout(username)
//    }


    override fun deleteUser(token: String): Single<Response<String>> {
        return api.deleteUser(token)
    }

    override var user: User?
        set(value) = cache.set(Constants.Cache.USER, value)
        get() = cache.get(Constants.Cache.USER)

}