package com.snt.phoney.domain.repository

import com.snt.phoney.domain.model.*
import io.reactivex.Single

interface OrderRepository {
    /**
     *@param token string 必须	用户token
     *@param type string 必须	订单类型订单类型 0 购买蜜币 1 购买会员 10 消费红包蜜币 11 消费解锁相册蜜币 12 语音蜜，31 蜜币提现
     *@param target string 必须  对应类型的uuid 购买蜜币(蜜币规则uuid) 购买会员(会员规则uuid) 消费红包蜜币（相片id） 消费解锁相册蜜币（相册id） 语音蜜（蜜币记录uuid）
     *@param uid string  非必须   目标用户uuid(当时红包和相册蜜币的时候，uid必填)
     */
    fun createOrder(token: String,
                    type: String,
                    target: String,
                    uid: String): Single<Response<String>>

    /**
     *@param token string 用户token
     *@param orderId string 订单唯一标示
     */
    fun payInMibi(token: String, orderId: String): Single<Response<String>>

    /**
     *@param token string 用户token
     *@param orderId string 订单唯一标示
     *
     *@return 返回微信统支付相关签名信息
     */
    fun wechatPay(token: String, orderId: String): Single<Response<WxPrePayResult>>

    /**
     *@param token string 用户token
     *@param orderId string 订单唯一标示
     *
     *@return 返回支付宝支付相关签名信息
     */
    fun alipay(token: String, orderId: String): Single<Response<String>>

    fun bindAlipay(token: String): Single<Response<String>>

    fun uploadAuthCode(token: String,authCode:String): Single<Response<String>>

    fun preWithdraw(token: String): Single<Response<PreWithdraw>>

    /**
     * @param money 金额
     * @return 返回用户提现唯一标示uuid
     */
    fun withdraw(token: String, money: Double): Single<Response<PreWithdraw>>

    /**
     *@param token    string	是	用户token
     *@param uuid    string	是	提现uuid
     */
    fun getWithdrawInfo(token: String, uuid: String): Single<Response<WithdrawInfo>>

    /**
     *@param token    string	是	用户token
     *@param type    string	是	类型 0 入账（充值明细） 1 消费明细
     *@param page    string	是	1
     *@param startTime    string	否	范围检索时候使用（开始日期 eg:2018-11-11)
     *@param endTime    string	否	结束日期，范围检索的时候传
     */
    fun listOrder(token: String,
                  type: String,
                  page: String,
                  startTime: String,
                  endTime: String): Single<Response<List<OrderRecord>>>

}