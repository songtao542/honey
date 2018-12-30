package com.snt.phoney.domain.repository


import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.*
import io.reactivex.Single
import java.io.File

interface UserRepository : UserAccessor {
    fun signup(phone: String,
               msgId: String,
               code: String,
               deviceToken: String,
               osVersion: String,
               version: String,
               mobilePlate: String): Single<Response<User>>

    fun bindPhone(msgId: String,
                  code: String,
                  phone: String,
                  uuid: String,
                  token: String): Single<Response<String>>

    fun signupByThirdPlatform(openId: String, //第三方openid（qq是uid）
                              thirdToken: String,
                              plate: String,
                              nickName: String,
                              headPic: String,
                              deviceToken: String,
                              osVersion: String,
                              version: String,
                              mobilePlate: String): Single<Response<User>>

    fun setUserSex(token: String, sex: Int): Single<Response<String>>

    fun uploadHeadIcon(token: String, file: File): Single<Response<String>>

    fun setUserFeatures(token: String,
                        height: Int,
                        weight: Int,
                        age: Int,
                        cup: String): Single<Response<String>>

    fun setUserInfo(token: String,
                    cities: String,
                    career: String,
                    program: String): Single<Response<String>>


    fun setFullUserInfo(token: String,
                        height: Int,
                        weight: Double,
                        age: Int,
                        cup: String,
                        cities: String,
                        introduce: String,
                        career: String,
                        program: String,
                        wechatAccount: String,
                        nickname: String,
                        price: Int): Single<Response<String>>

    fun listUser(
            token: String,
            latitude: String,
            longitude: String,
            type: String,
            pageIndex: Int,
            city: String,
            heightStart: String,
            heightEnd: String,
            ageStart: String,
            ageEnd: String,
            cupStart: String,
            cupEnd: String
    ): Single<Response<List<User>>>


    fun listRecommendUser(token: String,
                          longitude: String,
                          latitude: String,
                          pageIndex: Int): Single<Response<List<User>>>

    /**
     *  用户uuid
     */
    fun getUserInfo(token: String,
                    uid: String,
                    latitude: Double,
                    longitude: Double): Single<Response<User>>

    fun setWalletNewsToRead(token: String): Single<Response<String>>

    fun setPhotoPermission(token: String,
                           photoPermission: Int,
                           money: Double,
                           photoId: String): Single<Response<String>>

    fun uploadPhotos(token: String, photos: List<File>): Single<Response<List<Photo>>>

    fun deletePhotos(token: String, photoIds: List<String>): Single<Response<List<Photo>>>

    /**
     *@param token  string
     *@param latitude    string		纬度
     *@param longitude    string		经度
     *@param address  string	    可选(详细地址）
     *@param city   string		城市（必填）
     */
    fun updateUserLocation(token: String,
                           latitude: Double,
                           longitude: Double,
                           address: String,
                           city: String): Single<Response<String>>

    fun getUserPhotos(token: String): Single<Response<List<Photo>>>

    fun getUserWechatAccount(token: String, uid: String): Single<Response<String>>

    fun applyToViewPhotos(token: String, target: String): Single<Response<String>>

    fun listPhotoApply(token: String, page: Int): Single<Response<List<PhotoApply>>>

    fun reviewPhotoApply(token: String, uuid: String, state: Int): Single<Response<String>>

    fun getAllInfoOfUser(token: String): Single<Response<UserInfo>>


    fun listMyFollow(token: String, pageIndex: Int): Single<Response<List<User>>>
    fun listFollowMe(token: String, pageIndex: Int): Single<Response<List<User>>>
    fun listVisitor(token: String): Single<Response<List<User>>>

    fun follow(token: String, uuid: String): Single<Response<Boolean>>


    fun setPrivacyPassword(token: String,
                           password: String,
                           privatePassword: String): Single<Response<String>>

    fun hasPrivacyPassword(token: String): Single<Response<Boolean>>

    fun closePrivacyPassword(token: String): Single<Response<String>>

    fun listVipCombo(token: String): Single<Response<List<VipCombo>>>

    fun getMibiAmount(token: String): Single<Response<Int>>

    fun getMibiWallet(token: String): Single<Response<MibiWallet>>

    fun getVipInfo(token: String): Single<Response<VipInfo>>

//    fun login(username: String, password: String): LiveData<Response<User>>
//    fun resetPassword(key: String, password: String): LiveData<Response<String>>
//    fun logout(username: String): LiveData<Response<String>>

    fun requestVerificationCode(phone: String): Single<Response<String>>

    fun deleteUser(token: String): Single<Response<String>>

}