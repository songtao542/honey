package com.snt.phoney.api

import androidx.lifecycle.LiveData
import com.snt.phoney.domain.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") username: String, @Field("password") password: String): LiveData<Response<User>>

    @FormUrlEncoded
    @POST("user/login")
    fun resetPassword(@Field("username") username: String, @Field("password") password: String): LiveData<Response<String>>


    @FormUrlEncoded
    @POST("user/login")
    fun logout(@Field("username") username: String): LiveData<Response<String>>


    @GET("city/listCities")
    fun listCities(): Single<Response<List<Province>>>


    @FormUrlEncoded
    @POST("sms/bindMobile")
    fun bindPhone(@Field("msg_id") msgId: String,
                  @Field("code") code: String,
                  @Field("phone") phone: String,
                  @Field("uuid") uuid: String,
                  @Field("token") token: String): Single<Response<String>>

    /**
     *@param sex -1，未设置；0，男性；1，女性
     */
    @FormUrlEncoded
    @POST("users/setSex")
    fun setUserSex(@Field("token") token: String,
                   @Field("sex") sex: Int): Single<Response<String>>

    @Multipart
    @POST("users/setPortrait")
    fun uploadHeadIcon(@Part("token") token: String,
                       @Part portrait: MultipartBody.Part): Single<Response<String>>

    /**
     *@param token  string
     *@param lat    string		纬度
     *@param lng    string		经度
     *@param title  string	    可选(详细地址）
     *@param city   string		城市（必填）
     */
    @FormUrlEncoded
    @POST("users/updateUserLocation")
    fun updateUserLocation(@Field("token") token: String,
                           @Field("lng") latitude: String,
                           @Field("lat") longitude: String,
                           @Field("title") address: String,
                           @Field("city") city: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("users/setFeatures")
    fun setUserFeatures(@Field("token") token: String,
                        @Field("height") height: String,
                        @Field("weight") weight: String,
                        @Field("age") age: String,
                        @Field("cup") cup: String): Single<Response<String>>


    /**
     *@param citys 城市列表id用逗号分隔
     *@param career 职业 对于文字，接口返回
     *@param program 宣言 对应为文字 接口返回
     */
    @FormUrlEncoded
    @POST("users/setBaseInfo")
    fun setUserInfo(@Field("token") token: String,
                    @Field("citys") cities: String,
                    @Field("career") career: String,
                    @Field("program") program: String): Single<Response<String>>


    @FormUrlEncoded
    @POST("users/setUserInfo")
    fun setFullUserInfo(@Field("token") token: String,
                        @Field("height") height: String,// 	身高
                        @Field("weight") weight: String,//	体重
                        @Field("age") age: String,//	年龄
                        @Field("cup") cup: String,//	罩杯
                        @Field("citys") cities: String,//	常在城市
                        @Field("introduce") introduce: String,//	个人介绍
                        @Field("career") career: String,//	职业
                        @Field("program") program: String,//	约会节目
                        @Field("account_wx") wechatAccount: String,//	微信
                        @Field("nickName") nickname: String, //	昵称
                        @Field("price") price: String //
    ): Single<Response<String>>


    @FormUrlEncoded
    @POST("users/delUser")
    fun deleteUser(@Field("token") token: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("users/getUsersPhoto")
    fun getUserPhotos(@Field("token") token: String): Single<Response<List<Photo>>>

    @FormUrlEncoded
    @POST("users/homePage/getWxInfo")
    fun getUserWechatAccount(@Field("token") token: String,
                             @Field("uid") uid: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("users/homePage/photoApply")
    fun applyToViewPhotos(@Field("token") token: String,
                          @Field("target") target: String): Single<Response<String>>

    @GET("users/homePage/listPhotoApplys")
    fun listPhotoApply(@Query("token") token: String,
                       @Query("page") page: String): Single<Response<List<PhotoApply>>>

    /**
     *@param token    string	是	用户token
     *@param uuid    string	 是	申请uuid
     *@param state    string	是	0 同意 1 拒绝
     */
    @FormUrlEncoded
    @POST("users/homePage/photoApplyVerify")
    fun reviewPhotoApply(@Field("token") token: String,
                         @Field("uuid") uuid: String,
                         @Field("state") state: String): Single<Response<String>>

    /**
     * 获取访客列表
     */
    @FormUrlEncoded
    @POST("users/other/listVisitors")
    fun listVisitor(@Field("token") token: String): Single<Response<List<User>>>

    /**
     * 获取关注我的人列表
     */
    @FormUrlEncoded
    @POST("users/other/listCares")
    fun listMyFollow(@Field("token") token: String,
                     @Field("page") pageIndex: String): Single<Response<List<User>>>

    /**
     * 获取我关注的人列表
     */
    @FormUrlEncoded
    @POST("users/other/listFollows")
    fun listFollowMe(@Field("token") token: String,
                     @Field("page") pageIndex: String): Single<Response<List<User>>>

    @FormUrlEncoded
    @POST("users/other/care")
    fun follow(@Field("token") token: String,
               @Field("uid") uuid: String): Single<Response<Boolean>>

    @FormUrlEncoded
    @POST("users/other/amountInfo")
    fun getAllInfoOfUser(@Field("token") token: String): Single<Response<UserInfo>>

    @FormUrlEncoded
    @POST("users/homePage/home")
    fun getUserInfo(@Field("token") token: String,
                    @Field("uid") uid: String,    //	用户uuid
                    @Field("longitude") latitude: String,
                    @Field("latitude") longitude: String): Single<Response<User>>

    @FormUrlEncoded
    @POST("member/wallet/updateWalletChange2Read")
    fun setWalletNewsToRead(@Field("token") token: String): Single<Response<String>>


    /**
     * 发布约会
     *@param  token    string	是	用户会话token
     *@param  content    string	是	约会内容
     *@param  city    number	是	约会城市
     *@param  days    string	是	约会时长
     *@param  latitude    string	否	纬度（和常规颠倒）
     *@param  longitude    string	否	经度
     *@param  title    string	否	定位的逆地理位置，可选
     *@param  location    string	否	具体约会地点
     *@param  program    string	是	约会节目（接口返回，传字符串， eg:吃饭）
     */
    @Multipart
    //@Headers(value = ["Content-Type: application/x-www-form-urlencoded; charset=UTF-8"])
    @POST("appointment/addAppointment")
    fun publishDating(@Part("token") token: String,
                      @Part("title") title: String,
                      @Part("grogram") program: String,
                      @Part("content") content: String,
                      @Part("days") days: Int,
                      @Part("city") city: String,
                      @Part("location") location: String,
                      @Part("longitude") latitude: String,
                      @Part("latitude") longitude: String,
                      @Part cover: List<MultipartBody.Part>): Single<Response<String>>

    /**
     * 取消约会
     */
    @FormUrlEncoded
    @POST("appointment/cancelAppointment")
    fun cancelDating(@Field("token") token: String,
                     @Field("uuid") uuid: String): Single<Response<String>>

    /**
     * 参加约会
     */
    @FormUrlEncoded
    @POST("appointment/attendAppointment")
    fun joinDating(@Field("token") token: String,
                   @Field("uuid") uuid: String): Single<Response<String>>

    /**
     * 参加约会
     */
    @FormUrlEncoded
    @POST("appointment/cancelAppointmentItem")
    fun quitDating(@Field("token") token: String,
                   @Field("uuid") uuid: String): Single<Response<String>>

    /**
     * 查看约会
     */
    @FormUrlEncoded
    @POST("appointment/scanAppointment")
    fun viewDating(@Field("token") token: String,
                   @Field("uuid") uuid: String): Single<Response<String>>

    /**
     * 审核约会
     */
    @FormUrlEncoded
    @POST("appointment/auditAttendAppointment")
    fun reviewDating(@Field("token") token: String,
                     @Field("uuid") uuid: String,
                     @Field("state") state: String): Single<Response<String>>

    /**
     * 约会节目
     */
    @GET("users/other/listProgram")
    fun listDatingProgram(@Query("token") token: String,
                          @Query("uuid") uuid: String): Single<Response<List<DatingProgram>>>

    /**
     * 该用户发布的约会
     */
    @GET("appointment/listAppointmentsByUser")
    fun listDatingByUser(@Query("token") token: String,
                         @Query("uuid") uuid: String,
                         @Query("page") pageIndex: String): Single<Response<List<Dating>>>

    /**
     * 该用户发布的约会
     */
    @GET("appointment/listAppointmentPublish")
    fun listMyDating(@Query("token") token: String,
                     @Query("page") pageIndex: String): Single<Response<List<Dating>>>

    /**
     * 推荐约会
     */
    @GET("appointment/listAppointment")
    fun listRecommendDating(@Query("token") token: String,
                            @Query("page") pageIndex: String,
                            @Query("dateType") dateType: String,
                            @Query("distanceType") distanceType: String,
                            @Query("grogram") program: String,
                            @Query("latitude") longitude: String,
                            @Query("longitude") latitude: String): Single<Response<List<Dating>>>

    /**
     * 热门约会
     */
    @GET("appointment/listPopularAppointment")
    fun listPopularDating(@Query("token") token: String,
                          @Query("page") pageIndex: String): Single<Response<List<Dating>>>

    /**
     * 约会详情
     */
    @GET("appointment/detailAppointment")
    fun getDatingDetail(@Query("token") token: String,
                        @Query("uuid") uuid: String,
                        @Query("longitude") latitude: String,
                        @Query("latitude") longitude: String): Single<Response<Dating>>

    /**
     * 参加的约会
     */
    @GET("appointment/listAppointmentAttend")
    fun listJoinedDating(@Query("token") token: String,
                         @Query("page") pageIndex: String): Single<Response<List<Dating>>>

    /**
     * 被申请的约会
     * @param token
     * @param uuid    string 被申请的约会表uuid
     * @param pageIndex    string 分页码
     */
    @GET("appointment/listAttendedItem")
    fun listDatingApplicant(@Query("token") token: String,
                            @Query("uuid") uuid: String,
                            @Query("page") pageIndex: String): Single<Response<List<Applicant>>>

    /**
     * 被申请的约会
     * @param token
     * @param pageIndex  string 分页码
     */
    @GET("appointment/listAllAttendedItems")
    fun listApplicant(@Query("token") token: String,
                      @Query("page") pageIndex: String): Single<Response<List<Applicant>>>


    /**
     * @param password  MD5后的数字密码（32位）
     * @param privatePassword  倒叙密码后的MD5的数字密码（32位）
     */
    @FormUrlEncoded
    @POST("users/setPrivatePassword")
    fun setPrivacyPassword(@Field("token") token: String,
                           @Field("password") password: String,
                           @Field("privatePassword") privatePassword: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("users/closePrivatePassword")
    fun closePrivacyPassword(@Field("token") token: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("users/getPrivateStatus")
    fun hasPrivacyPassword(@Field("token") token: String): Single<Response<Boolean>>


    @FormUrlEncoded
    @POST("member/listMemberCombos")
    fun listVipCombo(@Field("token") token: String): Single<Response<List<VipCombo>>>

    @FormUrlEncoded
    @POST("member/getIntimateGold")
    fun getMibiAmount(@Field("token") token: String): Single<Response<Int>>

    @FormUrlEncoded
    @POST("member/wallet/myWallet")
    fun getMibiWallet(@Field("token") token: String): Single<Response<MibiWallet>>

    @FormUrlEncoded
    @POST("member/getMemberInfo")
    fun getVipInfo(@Field("token") token: String): Single<Response<VipInfo>>


    /**
     *@param token string 必须	用户token
     *@param type string 必须	订单类型订单类型 0 购买蜜币 1 购买会员 10 消费红包蜜币 11 消费解锁相册蜜币 12 语音蜜，31 蜜币提现
     *@param target string 必须  对应类型的uuid 购买蜜币(蜜币规则uuid) 购买会员(会员规则uuid) 消费红包蜜币（相片id） 消费解锁相册蜜币（相册id） 语音蜜（蜜币记录uuid）
     *@param uid string  非必须   目标用户uuid(当时红包和相册蜜币的时候，uid必填)
     */
    @FormUrlEncoded
    @POST("payment/order/unifiedOrder")
    fun createOrder(@Field("token") token: String,
                    @Field("type") type: String,
                    @Field("target") target: String,
                    @Field("uid") uid: String): Single<Response<String>>

    /**
     *@param token string 用户token
     *@param unifiedOrder string 订单唯一标示
     */
    @FormUrlEncoded
    @POST("payment/pay/payWithPhoney")
    fun payInMibi(@Field("token") token: String,
                  @Field("unifiedOrder") unifiedOrder: String): Single<Response<String>>

    /**
     *@param token string 用户token
     *@param unifiedOrder string 订单唯一标示
     *
     *@return 返回微信统支付相关签名信息
     */
    @FormUrlEncoded
    @POST("payment/pay/wechatPay")
    fun wechatPay(@Field("token") token: String,
                  @Field("unifiedOrder") unifiedOrder: String): Single<Response<WxPrePayResult>>

    /**
     *@param token string 用户token
     *@param unifiedOrder string 订单唯一标示
     *
     *@return 返回支付宝支付相关签名信息
     */
    @FormUrlEncoded
    @POST("payment/pay/alipay")
    fun alipay(@Field("token") token: String,
               @Field("unifiedOrder") unifiedOrder: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("payment/withdrawDeposit/preWithdraw")
    fun preWithdraw(@Field("token") token: String): Single<Response<PreWithdraw>>

    /**
     * @param money 金额
     * @return 返回用户提现唯一标示uuid
     */
    @FormUrlEncoded
    @POST("payment/withdrawDeposit/withdraw")
    fun withdraw(@Field("token") token: String,
                 @Field("money") money: Double): Single<Response<PreWithdraw>>

    /**
     *@param token    string	是	用户token
     *@param uuid    string	是	提现uuid
     */
    @FormUrlEncoded
    @POST("payment/withdrawDeposit/detailWithdraw")
    fun getWithdrawInfo(@Field("token") token: String,
                        @Field("uuid") uuid: String): Single<Response<WithdrawInfo>>

    /**
     *@param token    string	是	用户token
     *@param type    string	是	类型 0 入账（充值明细） 1 消费明细
     *@param page    string	是	1
     *@param startTime    string	否	范围检索时候使用（开始日期 eg:2018-11-11)
     *@param endTime    string	否	结束日期，范围检索的时候传
     */
    @FormUrlEncoded
    @POST("payment/order/listOrders")
    fun listOrder(@Field("token") token: String,
                  @Field("type") type: String,
                  @Field("page") page: String,
                  @Field("startTime") startTime: String,
                  @Field("endTime") endTime: String): Single<Response<List<OrderRecord>>>

    /**
     * @param photoPermission string 权限值(0 全部公开 1 红包（只有单张） 2 解锁相册(全部) 3 需要申请查看 4 不可查看)
     * @param money string	金额（红包类型必填）
     * @param money string	照片id（红包照片必填）
     */
    @FormUrlEncoded
    @POST("users/setPhotoRight")
    fun setPhotoPermission(@Field("token") token: String,
                           @Field("photoRight") photoPermission: Int,
                           @Field("money") money: Int,
                           @Field("id") photoId: String): Single<Response<String>>

    @Multipart
    @POST("users/setPhotos")
    fun uploadPhotos(@Part("token") token: String,
                     @Part photos: List<MultipartBody.Part>): Single<Response<List<Photo>>>

    @FormUrlEncoded
    @POST("users/setPhotos")
    fun deletePhotos(@Field("token") token: String,
                     @Field("ids") photoIds: List<String>): Single<Response<List<Photo>>>

    @FormUrlEncoded
    @POST("users/other/listCareer")
    fun listCareer(@Field("token") token: String): Single<Response<List<Career>>>

    @FormUrlEncoded
    @POST("users/other/listIntroduces")
    fun listPurpose(@Field("token") token: String): Single<Response<List<Purpose>>>

    @FormUrlEncoded
    @POST("message/listOfficesMessage")
    fun listOfficialMessage(@Field("token") token: String): Single<Response<List<OfficialMessage>>>

    @POST("report/listReportItems")
    fun listReportReasons(): Single<Response<List<ReportReason>>>

    /**
     *@param token    string	是	用户会话id
     *@param type    string	是	0、随机数 1、随机一段话
     */
    @GET("users/authentication/getRandomWords")
    fun getAuthRandomMessage(@Query("token") token: String,
                             @Query("type") type: String): Single<Response<String>>

    @GET("users/authentication/getAuthenticationState")
    fun getAuthState(@Query("token") token: String): Single<Response<AuthState>>

    /**
     *@param token    string	是	用户会话id
     *@param type    string	是	认证类型（0、照片 1、视频）
     *@param pauthentication    string	是	上传文件名
     */
    @Multipart
    @POST("users/authentication/addAuthentication")
    fun auth(@Part("token") token: String,
             @Part("type") type: String,
             @Part pauthentication: MultipartBody.Part): Single<Response<String>>

    /**
     *@param token    string	是	用户token
     *@param type    string	是	举报类型 id
     *@param targetUid    string	是	举报用户uid
     *@param content    string	是	举报内容
     *@param cover    string	是	图片上传参数名
     *@param rtype    string	是	0 用户 1 约会
     */
    @Multipart
    @POST("report/report")
    fun report(@Part("token") token: String,
               @Part("type") type: String,
               @Part("targetUid") targetUid: String,
               @Part("content") content: String,
               @Part("rtype") rtype: String,
               @Part cover: MultipartBody.Part
    ): Single<Response<String>>

    @FormUrlEncoded
    @POST("sms/sendMsg")
    fun sendMsg(@Field("phone") phone: String): Single<Response<String>>


    /**
     *@param   token:String	用户token
     *@param   lat:String	注意经纬度是反的（当type=3 必填）
     *@param   lng:String	（当type=3 必填）
     *@param   type:String	type 0 默认全部（按时间倒叙） 1 今日新人 2 人气最高 3 距离最近 4 身材最好 5 更多筛选 6 查找城市
     *@param   page:String	从1开始
     *@param   city:String	城市（当type=6 必填）
     *@param   height_start:String	身高开始（当type=5 必填）
     *@param   height_end:String	身高结束（当type=5 必填）
     *@param   age_start:String	年龄开始（当type=5 必填）
     *@param   age_end:String	年龄结束（当type=5 必填）
     *@param   cup_start:String	罩杯开始（当type=5 必填）30A---45G
     *@param   cup_end:String	罩杯结束（当type=5 必填）
     */
    @FormUrlEncoded
    @POST("users/listUsers")
    fun listUser(
            @Field("token") token: String,
            @Field("lng") latitude: String,
            @Field("lat") longitude: String,
            @Field("type") type: String,
            @Field("page") page: String,
            @Field("city") city: String,
            @Field("height_start") heightStart: String,
            @Field("height_end") heightEnd: String,
            @Field("age_start") ageStart: String,
            @Field("age_end") ageEnd: String,
            @Field("cup_start") cupStart: String,
            @Field("cup_end") cupEnd: String
    ): Single<Response<List<User>>>


    /**
     *@param token    string	是	用户token
     *@param latitude    string	是	纬度（与实际的纬度值）
     *@param longitude    string	是	经度
     *@param page    string	是	1
     */
    @FormUrlEncoded
    @POST("users/listRecommendsUsers")
    fun listRecommendUser(@Field("token") token: String,
                          @Field("latitude") longitude: String,
                          @Field("longitude") latitude: String,
                          @Field("page") pageIndex: String
    ): Single<Response<List<User>>>

    @FormUrlEncoded
    @POST("users/third/register")
    fun signupByThirdPlatform(@Field("openId") openId: String, //第三方openid（qq是uid）
                              @Field("third_token") thirdToken: String,
                              @Field("plate") plate: String,
                              @Field("nickName") nickName: String,
                              @Field("headPic") headPic: String,
                              @Field("deviceToken") deviceToken: String,
                              @Field("osVersion") osVersion: String,
                              @Field("version") version: String,
                              @Field("mobilePlate") mobilePlate: String): Single<Response<User>>

    @FormUrlEncoded
    @POST("sms/registerWithMsgCode")
    fun signupByMsgCode(@Field("phone") phone: String,
                        @Field("msg_id") msgId: String,
                        @Field("code") code: String,
                        @Field("deviceToken") deviceToken: String,
                        @Field("osVersion") osVersion: String,
                        @Field("version") version: String,
                        @Field("mobilePlate") mobilePlate: String): Single<Response<User>>


}