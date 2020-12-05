package com.snt.phoney.api

import androidx.lifecycle.LiveData
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.snt.phoney.domain.model.*
import com.snt.phoney.utils.life.SingleLiveData
import io.reactivex.Single
import okhttp3.MultipartBody

class MockApi : Api {
    override fun login(username: String, password: String): LiveData<Response<User>> {
        return SingleLiveData.of(Response.of(MockResponse.user(0)))
    }

    override fun resetPassword(username: String, password: String): LiveData<Response<String>> {
        return SingleLiveData.of(Response.of("success"))
    }

    override fun resetPassword(token: String, password: String, privatePassword: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun logout(username: String): LiveData<Response<String>> {
        return SingleLiveData.of(Response.of("success"))
    }

    override fun listCities(): Single<Response<List<Province>>> {
        return Single.create<Response<List<Province>>> { it.onSuccess(Response.of(MockResponse.provinceList())) }
    }

    override fun bindPhone(msgId: String, code: String, phone: String, uuid: String, token: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun setUserSex(token: String, sex: Int): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun updateUserLocation(token: String, latitude: String, longitude: String, address: String, city: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun setUserFeatures(token: String, height: String, weight: String, age: String, cup: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun setUserInfo(token: String, cities: String, career: String, program: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun setFullUserInfo(token: String, height: String, weight: String, age: String, cup: String, cities: String, introduce: String, career: String, program: String, wechatAccount: String, nickname: String, price: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun deleteUser(token: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun getUserPhotos(token: String): Single<Response<List<Photo>>> {
        return Single.create<Response<List<Photo>>> { it.onSuccess(Response.of(MockResponse.photoList())) }
    }

    override fun getPhotosPrice(token: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun getUserWechatAccount(token: String, uid: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun getResetPasswordState(token: String): Single<Response<Int>> {
        return Single.create<Response<Int>> { it.onSuccess(Response.of(1)) }
    }

    override fun uploadResetPasswordFile(token: MultipartBody.Part, file: MultipartBody.Part): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun cancelResetPassword(token: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun applyToViewPhotos(token: String, target: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun listPhotoApply(token: String, page: String): Single<Response<List<PhotoApply>>> {
        return Single.create<Response<List<PhotoApply>>> { it.onSuccess(Response.of(MockResponse.photoApplyList())) }
    }

    override fun reviewPhotoApply(token: String, uuid: String, state: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun listVisitor(token: String): Single<Response<List<User>>> {
        return Single.create<Response<List<User>>> { it.onSuccess(Response.of(MockResponse.userList())) }
    }

    override fun listMyFollow(token: String, pageIndex: String): Single<Response<List<User>>> {
        return Single.create<Response<List<User>>> { it.onSuccess(Response.of(MockResponse.userList())) }
    }

    override fun listFollowMe(token: String, pageIndex: String): Single<Response<List<User>>> {
        return Single.create<Response<List<User>>> { it.onSuccess(Response.of(MockResponse.userList())) }
    }

    override fun follow(token: String, uuid: String): Single<Response<Boolean>> {
        return Single.create<Response<Boolean>> { it.onSuccess(Response.of(true)) }
    }

    override fun getAllInfoOfUser(token: String): Single<Response<UserInfo>> {
        return Single.create<Response<UserInfo>> { it.onSuccess(Response.of(MockResponse.userInfo())) }
    }

    override fun getUserInfo(token: String, uid: String, latitude: String, longitude: String): Single<Response<User>> {
        return Single.create<Response<User>> { it.onSuccess(Response.of(MockResponse.user(0))) }
    }

    override fun setWalletNewsToRead(token: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun publishDating(token: MultipartBody.Part, title: MultipartBody.Part, program: MultipartBody.Part, content: MultipartBody.Part, days: MultipartBody.Part, city: MultipartBody.Part, location: MultipartBody.Part, latitude: MultipartBody.Part, longitude: MultipartBody.Part, cover: List<MultipartBody.Part>): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun cancelDating(token: String, uuid: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun joinDating(token: String, uuid: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun quitDating(token: String, uuid: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun viewDating(token: String, uuid: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun reviewDating(token: String, uuid: String, state: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun listDatingProgram(token: String, uuid: String): Single<Response<List<DatingProgram>>> {
        return Single.create<Response<List<DatingProgram>>> { it.onSuccess(Response.of(MockResponse.datingProgramList())) }
    }

    override fun listDatingByUser(token: String, uuid: String, pageIndex: String): Single<Response<List<Dating>>> {
        return Single.create<Response<List<Dating>>> { it.onSuccess(Response.of(MockResponse.datingList())) }
    }

    override fun listMyDating(token: String, pageIndex: String): Single<Response<List<Dating>>> {
        return Single.create<Response<List<Dating>>> { it.onSuccess(Response.of(MockResponse.datingList())) }
    }

    override fun listRecommendDating(token: String, pageIndex: String, dateType: String, distanceType: String, program: String, longitude: String, latitude: String): Single<Response<List<Dating>>> {
        return Single.create<Response<List<Dating>>> { it.onSuccess(Response.of(MockResponse.datingList())) }
    }

    override fun listPopularDating(token: String, pageIndex: String): Single<Response<List<Dating>>> {
        return Single.create<Response<List<Dating>>> { it.onSuccess(Response.of(MockResponse.datingList())) }
    }

    override fun getDatingDetail(token: String, uuid: String, latitude: String, longitude: String): Single<Response<Dating>> {
        return Single.create<Response<Dating>> { it.onSuccess(Response.of(MockResponse.dating(0))) }
    }

    override fun listJoinedDating(token: String, pageIndex: String): Single<Response<List<Dating>>> {
        return Single.create<Response<List<Dating>>> { it.onSuccess(Response.of(MockResponse.datingList())) }
    }

    override fun listDatingApplicant(token: String, uuid: String, pageIndex: String): Single<Response<List<Applicant>>> {
        return Single.create<Response<List<Applicant>>> { it.onSuccess(Response.of(MockResponse.applicantList())) }
    }

    override fun listApplicant(token: String, pageIndex: String): Single<Response<List<Applicant>>> {
        return Single.create<Response<List<Applicant>>> { it.onSuccess(Response.of(MockResponse.applicantList())) }
    }

    override fun setPrivacyPassword(token: String, password: String, privatePassword: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun closePrivacyPassword(token: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun hasPrivacyPassword(token: String): Single<Response<Boolean>> {
        return Single.create<Response<Boolean>> { it.onSuccess(Response.of(true)) }
    }

    override fun listMemberCombo(token: String): Single<Response<List<MemberCombo>>> {
        return Single.create<Response<List<MemberCombo>>> { it.onSuccess(Response.of(MockResponse.memberComboList())) }
    }

    override fun getMibiAmount(token: String): Single<Response<Int>> {
        return Single.create<Response<Int>> { it.onSuccess(Response.of(456)) }
    }

    override fun getMibiWallet(token: String): Single<Response<MibiWallet>> {
        return Single.create<Response<MibiWallet>> { it.onSuccess(Response.of(MockResponse.mibiWallet())) }
    }

    override fun getMemberInfo(token: String): Single<Response<MemberInfo>> {
        return Single.create<Response<MemberInfo>> { it.onSuccess(Response.of(MockResponse.memberInfo())) }
    }

    override fun createOrder(token: String, type: String, target: String, uid: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun payInMibi(token: String, unifiedOrder: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun wechatPay(token: String, unifiedOrder: String): Single<Response<WxPrePayResult>> {
        return Single.create<Response<WxPrePayResult>> { it.onSuccess(Response.of(MockResponse.wxPrePayResult())) }
    }

    override fun alipay(token: String, unifiedOrder: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun bindAlipay(token: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun uploadAuthCode(token: String, authCode: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun preWithdraw(token: String): Single<Response<PreWithdraw>> {
        return Single.create<Response<PreWithdraw>> { it.onSuccess(Response.of(MockResponse.preWithdraw())) }
    }

    override fun withdraw(token: String, money: Double): Single<Response<PreWithdraw>> {
        return Single.create<Response<PreWithdraw>> { it.onSuccess(Response.of(MockResponse.preWithdraw())) }
    }

    override fun getWithdrawInfo(token: String, uuid: String): Single<Response<WithdrawInfo>> {
        return Single.create<Response<WithdrawInfo>> { it.onSuccess(Response.of(MockResponse.withdrawInfo())) }
    }

    override fun listOrder(token: String, type: String, page: String, startTime: String, endTime: String): Single<Response<List<OrderRecord>>> {
        return Single.create<Response<List<OrderRecord>>> { it.onSuccess(Response.of(MockResponse.orderRecordList())) }
    }

    override fun setPhotoPermission(token: String, photoPermission: Int, money: String, photoId: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun uploadPhotos(token: MultipartBody.Part, photos: List<MultipartBody.Part>): Single<Response<List<Photo>>> {
        return Single.create<Response<List<Photo>>> { it.onSuccess(Response.of(MockResponse.photoList())) }
    }

    override fun uploadHeadIcon(token: MultipartBody.Part, portrait: MultipartBody.Part): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun deletePhotos(token: String, photoIds: List<String>): Single<Response<List<Photo>>> {
        return Single.create<Response<List<Photo>>> { it.onSuccess(Response.of(MockResponse.photoList())) }
    }

    override fun listCareer(token: String): Single<Response<List<Career>>> {
        return Single.create<Response<List<Career>>> { it.onSuccess(Response.of(MockResponse.careerList())) }
    }

    override fun listPurpose(token: String): Single<Response<List<Purpose>>> {
        return Single.create<Response<List<Purpose>>> { it.onSuccess(Response.of(MockResponse.purposeList())) }
    }

    override fun burnPhoto(token: String, target: String, id: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun listOfficialMessage(token: String): Single<Response<List<OfficialMessage>>> {
        return Single.create<Response<List<OfficialMessage>>> { it.onSuccess(Response.of(MockResponse.officialMessageList())) }
    }

    override fun listReportReasons(): Single<Response<List<ReportReason>>> {
        return Single.create<Response<List<ReportReason>>> { it.onSuccess(Response.of(MockResponse.reportReasonList())) }
    }

    override fun getAuthRandomMessage(token: String, type: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun getAuthState(token: String): Single<Response<AuthState>> {
        return Single.create<Response<AuthState>> { it.onSuccess(Response.of(MockResponse.authState())) }
    }

    override fun auth(token: MultipartBody.Part, type: MultipartBody.Part, pauthentication: MultipartBody.Part): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun report(token: MultipartBody.Part, type: MultipartBody.Part, targetUid: MultipartBody.Part, content: MultipartBody.Part, rtype: MultipartBody.Part, cover: MultipartBody.Part): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun sendMsg(phone: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun listUser(token: String, latitude: String, longitude: String, type: String, page: String, city: String, heightStart: String, heightEnd: String, ageStart: String, ageEnd: String, cupStart: String, cupEnd: String): Single<Response<List<User>>> {
        return Single.create<Response<List<User>>> { it.onSuccess(Response.of(MockResponse.userList())) }
    }

    override fun listRecommendUser(token: String, longitude: String, latitude: String, pageIndex: String): Single<Response<List<User>>> {
        return Single.create<Response<List<User>>> { it.onSuccess(Response.of(MockResponse.userList())) }
    }

    override fun signupByThirdPlatform(openId: String, thirdToken: String, plate: String, nickName: String, headPic: String, deviceToken: String, osVersion: String, version: String, mobilePlate: String): Single<Response<User>> {
        return Single.create<Response<User>> { it.onSuccess(Response.of(MockResponse.user(0))) }
    }

    override fun signupByMsgCode(phone: String, msgId: String, code: String, deviceToken: String, osVersion: String, version: String, mobilePlate: String): Single<Response<User>> {
        return Single.create<Response<User>> { it.onSuccess(Response.of(MockResponse.user(0))) }
    }

    override fun listNews(page: String): Single<Response<List<News>>> {
        return Single.create<Response<List<News>>> { it.onSuccess(Response.of(MockResponse.newsList())) }
    }

    override fun testSignGet(token: String, page: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

    override fun testSignPost(token: String, page: String): Single<Response<String>> {
        return Single.create<Response<String>> { it.onSuccess(Response.of("success")) }
    }

}