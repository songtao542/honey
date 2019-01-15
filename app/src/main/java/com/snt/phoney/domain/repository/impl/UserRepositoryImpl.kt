package com.snt.phoney.domain.repository.impl


import com.snt.phoney.api.Api
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.*
import com.snt.phoney.domain.persistence.PhotoDao
import com.snt.phoney.domain.repository.UserRepository
import com.snt.phoney.utils.media.MultipartUtil
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val userAccessor: UserAccessor,
                                             private val api: Api,
                                             private val photoDao: PhotoDao) : UserRepository, UserAccessor by userAccessor {
    override fun setPrivacyPassword(token: String, password: String, privatePassword: String): Single<Response<String>> {
        return api.setPrivacyPassword(token, password, privatePassword)
    }

    override fun hasPrivacyPassword(token: String): Single<Response<Boolean>> {
        return api.hasPrivacyPassword(token)
    }

    override fun closePrivacyPassword(token: String): Single<Response<String>> {
        return api.closePrivacyPassword(token)
    }

    override fun listVipCombo(token: String): Single<Response<List<VipCombo>>> {
        return api.listVipCombo(token)
    }

    override fun getMibiAmount(token: String): Single<Response<Int>> {
        return api.getMibiAmount(token)
    }

    override fun getMibiWallet(token: String): Single<Response<MibiWallet>> {
        return api.getMibiWallet(token)
    }

    override fun getVipInfo(token: String): Single<Response<VipInfo>> {
        return api.getVipInfo(token)
    }

    override fun burnPhoto(token: String, target: String, id: String): Single<Response<String>> {
        return api.burnPhoto(token, target, id)
    }

    override fun burnPhoto(photo: Photo) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                photoDao.insertPhoto(photo)
                val token = getAccessToken()
                if (token != null) {
                    burnPhoto(token, photo.ownerId ?: "", photo.id.toString())
                            .subscribeBy(
                                    onSuccess = {
                                        if (it.success) {
                                            photoDao.delete(photo.id)
                                        }
                                    },
                                    onError = {

                                    }
                            )
                }
            }
        }
    }

    override fun setPhotoPermission(token: String, photoPermission: Int, money: Double, photoId: String): Single<Response<String>> {
        val price = money.toInt()
        return api.setPhotoPermission(token, photoPermission, if (price > 0) price.toString() else "", photoId)
    }

    override fun uploadPhotos(token: String, photos: List<File>): Single<Response<List<Photo>>> {
        val photoParts = MultipartUtil.getMultipartList("photos", photos)
        return api.uploadPhotos(token, photoParts)
    }

    override fun deletePhotos(token: String, photoIds: List<String>): Single<Response<List<Photo>>> {
        return api.deletePhotos(token, photoIds)
    }

    override fun updateUserLocation(token: String, latitude: Double, longitude: Double, address: String, city: String): Single<Response<String>> {
        return api.updateUserLocation(token, latitude.toString(), longitude.toString(), address, city)
    }

    override fun getUserPhotos(token: String): Single<Response<List<Photo>>> {
        return api.getUserPhotos(token)
    }

    override fun getUserWechatAccount(token: String, uid: String): Single<Response<String>> {
        return api.getUserWechatAccount(token, uid)
    }

    override fun applyToViewPhotos(token: String, target: String): Single<Response<String>> {
        return api.applyToViewPhotos(token, target)
    }

    override fun listPhotoApply(token: String, page: Int): Single<Response<List<PhotoApply>>> {
        return api.listPhotoApply(token, page.toString())
    }

    override fun reviewPhotoApply(token: String, uuid: String, state: Int): Single<Response<String>> {
        return api.reviewPhotoApply(token, uuid, state.toString())
    }

    override fun listVisitor(token: String): Single<Response<List<User>>> {
        return api.listVisitor(token)
    }

    override fun getAllInfoOfUser(token: String): Single<Response<UserInfo>> {
        return api.getAllInfoOfUser(token)
    }

    override fun follow(token: String, uuid: String): Single<Response<Boolean>> {
        return api.follow(token, uuid)
    }

    override fun listMyFollow(token: String, pageIndex: Int): Single<Response<List<User>>> {
        return api.listMyFollow(token, pageIndex.toString())
    }

    override fun listFollowMe(token: String, pageIndex: Int): Single<Response<List<User>>> {
        return api.listFollowMe(token, pageIndex.toString())
    }

    override fun setFullUserInfo(token: String, height: Int, weight: Double, age: Int, cup: String, cities: String, introduce: String, career: String, program: String, wechatAccount: String, nickname: String, price: Int): Single<Response<String>> {
        return api.setFullUserInfo(token, height.toString(), weight.toInt().toString(), age.toString(), cup, cities, introduce, career, program, wechatAccount, nickname, price.toString())
    }

    override fun getUserInfo(token: String, uid: String, latitude: Double, longitude: Double): Single<Response<User>> {
        return api.getUserInfo(token, uid, latitude.toString(), longitude.toString())
    }

    override fun setWalletNewsToRead(token: String): Single<Response<String>> {
        return api.setWalletNewsToRead(token)
    }

    override fun listUser(token: String, latitude: String, longitude: String, type: String, pageIndex: Int, city: String,
                          heightStart: String, heightEnd: String,
                          ageStart: String, ageEnd: String,
                          cupStart: String, cupEnd: String): Single<Response<List<User>>> {
        return api.listUser(token, latitude, longitude, type, pageIndex.toString(), city, heightStart, heightEnd, ageStart, ageEnd, cupStart, cupEnd)
    }

    override fun listRecommendUser(token: String, latitude: String, longitude: String, pageIndex: Int): Single<Response<List<User>>> {
        return api.listRecommendUser(token, latitude, longitude, pageIndex.toString())
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

    override fun uploadHeadIcon(token: String, file: File): Single<Response<String>> {
        val portrait = MultipartUtil.getMultipart("portrait", file)
        return api.uploadHeadIcon(token, portrait)
    }

    override fun setUserFeatures(token: String,
                                 height: Int,
                                 weight: Int,
                                 age: Int,
                                 cup: String): Single<Response<String>> {
        return api.setUserFeatures(token, height.toString(), weight.toString(), age.toString(), cup)
    }

    override fun deleteUser(token: String): Single<Response<String>> {
        return api.deleteUser(token)
    }

    override fun getResetPasswordState(token: String): Single<Response<Int>> {
        return api.getResetPasswordState(token)
    }

    override fun uploadResetPasswordFile(token: String, file: File): Single<Response<String>> {
        val part = MultipartUtil.getMultipart("pauthentication", file)
        return api.uploadResetPasswordFile(token, part)
    }

    override fun cancelResetPassword(token: String): Single<Response<String>> {
        return api.cancelResetPassword(token)
    }

    override fun resetPassword(token: String, password: String, privatePassword: String): Single<Response<String>> {
        return api.resetPassword(token, password, privatePassword)
    }

}



