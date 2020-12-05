package com.snt.phoney.api

import com.google.gson.annotations.SerializedName
import com.snt.phoney.domain.model.*
import io.reactivex.Single

object MockResponse {

    fun photo(i: Int): Photo {
        return Photo(
                id = i.toLong(),
                path = MockPictures[i], //   "path": "http://phoney.alance.pub/phoney/phoney/512/users/photos/u201811081829582566215b5a5a3b4f/1542507758452-5-a61dd1050281015775b699c828a394bc.jpg",
                flag = 0, //0 正常 ， 1 是红包
                price = 0,
                burn = 0,//  是否已焚，0 未焚，1 已焚
                paid = true,//红包的时候，已支付1，未支付0
                burnTime = 4000,
                ownerId = "$i", //该相片所有者的 uuid
                viewerId = "${i + 1}")
    }

    fun photoList(): List<Photo> {
        val photos = ArrayList<Photo>()
        for (i in 0..10) {
            photos.add(photo(i))
        }
        return photos
    }

    fun imUser(): ImUser {
        return ImUser(
                id = 0,
                password = "ca66dbe9584065332f917846a670aeaf",//":"ca66dbe9584065332f917846a670aeaf",
                username = "imu2018112816521080723aa89d706b15",//":"imu2018112816521080723aa89d706b15"},
                nickname = "淞",//":"淞",
                avatar = MockPictures[0] //":"淞",
        )
    }

    fun user(i: Int): User {
        return User(
                id = i.toLong(),
                uuid = "u20181204173309703e6c143d35a6a7",
                /**
                 * 用于JPush alias
                 */
                deviceToken = "u20181204173309703e6c143d35a6a7",
                username = "song",
                nickname = "淞",
                email = "924750553@qq.com",
                /**
                 * 隐私密码，正序 md5
                 */
                password = "73309703e6c143d35a6a7",
                /**
                 * 隐私密码，反序 md5
                 */
                privacyPassword = "73309703e6c143d35a6a7",
                phone = "13419517415",
                sex = 0,
                height = 160,
                weight = 50.0,
                age = 30,
                cup = "E",
                purpose = "交友",
                city = "深圳市",
                cities = cityList(i),
                career = "按摩师",
                avatar = MockPictures[i],//肖像
                introduce = "你好",
                photos = photoList(),
                photoPermission = 0,
                /**
                 * -1未申请, 0 审核中, 1 通过, 2 被拒绝
                 */
                photoApplyStatus = 1,
                /**
                 * 相册价格，当相册权限为 {@link PhotoPermission#LOCKED} 时，需要解锁相册的价格
                 */
                photoPrice = 0,
                photoId = i,
                /**
                 * 标识是否已经支付了相册蜜币
                 */
                isPhotoFree = true,
                hasWechatAccount = true,
                wechatAccount = "songtao",
                member = 1,
                memberEndTime = 0,
                token = "73309703e6c143d35a6a7",
                open = 0,
                price = 0.0,
                /**
                 * 0 未认证  2 已认证
                 */
                state = 2,
                tag = "This is tag!",
                distance = 0.0,
                program = "This is program!",
                im = imUser(),
                isCared = false,
                caredSize = 0,
                updateTime = 0,
                burnTime = 0)
    }

    fun userList(): List<User> {
        val list = ArrayList<User>()
        for (i in 0..10) {
            list.add(user(i))
        }
        return list
    }

    fun userInfo(): UserInfo {
        return UserInfo(
                countVisitor = 450,
                countFollowed = 140,
                countDating = 560,
                authState = AuthState(
                        message = "您还未通过纯蜜官方认证！",// "msg": "您还未通过纯蜜官方认证！"
                        score = "您还未通过纯蜜官方真人验证，靠谱值为0",//"score": "您还未通过纯蜜官方真人验证，靠谱值为0",
                        /**
                         * 0 未认证  1 认证中  2 认证通过  3 认证未通过
                         */
                        state = 0//"state": 0
                ),
                hasNewsOfDating = false,//  "message_appointment": false,
                hasNewsOfWallet = false,//"message_wallet": false,
                memberInfo = MemberInfo(
                        /**
                         * 会员开始时间
                         */
                        startTime = 0,// "startTime": null,
                        /**
                         * 会员到期时间
                         */
                        endTime = 0,//"endTime": null,
                        /**
                         *  1 是会员， 0 普通会员
                         */
                        member = 0//  "member": 0,
                )
        )
    }

    fun datingProgramList(): List<DatingProgram> {
        val list = ArrayList<DatingProgram>()
        for (i in 0..20) {
            list.add(DatingProgram(
                    id = i.toLong(),
                    name = "来约会吧$i")
            )
        }
        return list
    }

    fun dating(i: Int): Dating {
        val photos = ArrayList<Photo>()
        for (j in 0..10) {
            photos.add(photo(j))
        }
        return Dating(
                cover = photos,//  "cover": [
                isCared = false,//  "isCared": false,
                user = user(i),//   "user":
                createTime = 0,//  "ctime": 1542381337650,
                /**
                 * 0 未开始 1 进行中 2 过期 3 结束 5 取消
                 */
                state = 0,//  "state": 2,
                /**
                 *   0 审核中  1 通过  2 拒绝   3 取消
                 */
                applyState = 0,//  "state": 2,
                uuid = "a201811162315375b737eb9a223d591",//  "uuid": "a201811162315375b737eb9a223d591",
                content = "3455555555", // "content": "3455555555"
                distance = 64.6999,//"distance": 64.69999999999999,
                city = "深圳市",//"city": "深圳市",
                program = "看电影",//"grogram": "看电影",
                isAttend = false,//"isAttend": false,
                location = "广东省,深圳市, 福田区, 港中旅大厦",//"location": "广东省,深圳市, 福田区, 港中旅大厦",
                startTime = 1542470400000,//"startTime": 1542470400000,
                endTime = 1542556800000,//"endTime": 1542556800000
                attendNumber = 1542556800,//"attendNum": 1542556800000
                nickname = "淞"// "nickName": "xxx",
        )
    }

    fun datingList(): List<Dating> {
        val list = ArrayList<Dating>()
        for (i in 0..20) {
            list.add(dating(i))
        }
        return list
    }

    fun applicant(i: Int): Applicant {
        return Applicant(
                /**
                 * "nickName ,introduce,portrait,uuid"
                 */
                user = user(i),// "userInfo": {
                datingId = "a2018112122393393b32fb96cde44d4",//  "appointmentUuid": "a2018112122393393b32fb96cde44d4",
                uuid = "ai201811222319267ca3685594bdec2b",// "appointmentItemUuid": "ai201811222319267ca3685594bdec2b",
                createTime = 1542899966149,//  "ctime": 1542899966149,
                /**
                 *  0 审核中  1 通过  2 拒绝   3 取消
                 */
                state = 0//  "state": 0      // 0 审核中  1 通过  2 拒绝   3 取消
        )
    }

    fun applicantList(): List<Applicant> {
        val list = ArrayList<Applicant>()
        for (i in 0..20) {
            list.add(applicant(i))
        }
        return list
    }

    fun memberCombo(): MemberCombo {
        return MemberCombo(
                uuid = "mc2018111410284561ba1deedcb34657",//"uuid": "mc2018111410284561ba1deedcb34657"
                number = 0.5,//  "number": 0.5,
                recommend = 0,//  "isRecommend": 0,
                price = 30.0,//"price": 240,
                type = 0// "type": 0,
        )
    }

    fun memberComboList(): List<MemberCombo> {
        val list = ArrayList<MemberCombo>()
        for (i in 0..20) {
            list.add(memberCombo())
        }
        return list
    }

    fun mibiRule(): MibiRule {
        return MibiRule(
                uuid = "pr20181113182346f660abec7a88c33c",// "uuid": "pr20181113182346f660abec7a88c33c"
                money = 12,//  "money": 12,
                mibi = 180// "": 180,
        )
    }

    fun mibiRuleList(): List<MibiRule> {
        val list = ArrayList<MibiRule>()
        for (i in 0..20) {
            list.add(mibiRule())
        }
        return list
    }

    fun mibiWallet(): MibiWallet {
        return MibiWallet(
                mibi = 0,
                rules = mibiRuleList()
        )
    }

    fun memberInfo(): MemberInfo {
        return MemberInfo(
                /**
                 * 会员开始时间
                 */
                startTime = 0,// "startTime": null,
                /**
                 * 会员到期时间
                 */
                endTime = 0,//"endTime": null,
                /**
                 *  1 是会员， 0 普通会员
                 */
                member = 0//  "member": 0,
        )
    }

    fun wxPrePayResult(): WxPrePayResult {
        return WxPrePayResult(
                appid = "wxa876e58bf61e9dac",//   "appid": "wxa876e58bf61e9dac",
                partnerid = "1518670541",//"partnerid": "1518670541",
                prepayid = "wx191853538479007a399f88f21490905448",//"prepayid": "wx191853538479007a399f88f21490905448",
                noncestr = "35ed633d304cd5fbccc01d125fbe6d27",//"noncestr": "35ed633d304cd5fbccc01d125fbe6d27",
                timestamp = "1545216834",//  "timestamp" : "1545216834",
                pack = "Sign=WXPay",//"package": "Sign=WXPay",
                sign = "54A7514D0FE5BA1FC687231F7CB02063"//"paySign": "54A7514D0FE5BA1FC687231F7CB02063"
        )
    }

    fun preWithdraw(): PreWithdraw {
        return PreWithdraw(
                rule = "10蜜币等于人民币 0.50¥",//   "frule": "10蜜币等于人民币 0.50¥",
                limit = "每日限额1000元",//"flimit": "每日限额1000元",
                available = "15.50",// "avaliable": "15.50",
                isAlipayBind = false,//"alipay_bind": true,
                info = "最低30，最高1000元（48小时到账）"//"info": "最低30，最高1000元（48小时到账）"
        )
    }


    fun withDrawItem(): WithDrawItem {
        return WithDrawItem(
                reason = "系统触发",//  "reason": "系统触发",
                createTime = 1544583177404,//    "ctime": 1544583177404,
                state = 0,//    "state": 1,
                title = "发起提现申请"//    "title": "发起提现申请"
        )
    }

    fun withDrawItemList(): List<WithDrawItem> {
        val list = ArrayList<WithDrawItem>()
        for (i in 0..20) {
            list.add(withDrawItem())
        }
        return list
    }

    fun withdrawInfo(): WithdrawInfo {
        return WithdrawInfo(
                id = 0,//"id": 1,
                uuid = "uo2018121210525700bf9b1b6694bd8b",//"uuid": "uo2018121210525700bf9b1b6694bd8b",
                state = 0,//"state": 0,
                stateMessage = "处理中",//   "state_msg": "处理中",
                typeMessage = "支付宝提现",//"type_msg": "支付宝提现",
                type = 1,//"type": 1,
                money = 15.0,//"money": 15,
                openid = "支付宝账号 208****2392",//"openid": "支付宝账号 208****2392",
                createTime = 1544583177404,//"ctime": 1544583177404,
                items = withDrawItemList() //"items": [
        )
    }

    fun orderRecord(): OrderRecord {
        return OrderRecord(
                uuid = "uo20181201163756f5b309a2699d6928",//"uuid": "uo20181201163756f5b309a2699d6928",
                type = 0,//"type": 0,
                payType = "微信支付",//   "payType": "微信支付",
                title = "蜜币充值",//"title": "蜜币充值",
                price = "-158.0元",//"price": "-158.0元",
                info = "余额：10000",//"info": "余额：10000",
                createTime = 1543653476374//"ctime": 1543653476374,
        )
    }

    fun orderRecordList(): List<OrderRecord> {
        val list = ArrayList<OrderRecord>()
        for (i in 0..20) {
            list.add(orderRecord())
        }
        return list
    }

    fun career(i: Int): Career {
        return Career(
                id = i.toLong(),
                name = "This is career!"
        )
    }

    fun careerList(): List<Career> {
        val list = ArrayList<Career>()
        for (i in 0..20) {
            list.add(career(i))
        }
        return list
    }

    fun purpose(i: Int): Purpose {
        return Purpose(
                id = i.toLong(),
                name = "This is Purpose!"
        )
    }

    fun purposeList(): List<Purpose> {
        val list = ArrayList<Purpose>()
        for (i in 0..20) {
            list.add(purpose(i))
        }
        return list
    }

    fun officialMessage(i: Int): OfficialMessage {
        return OfficialMessage(
                uuid = "m2018120413455836f184d730c1dad0",//"uuid": "m2018120413455836f184d730c1dad0",,,,,
                cover = MockPictures[i],//    "cover": "http://phoney.alance.pub/phoney/phoney/512/message/cover/1543919621909-0-4e45bea8293cf3884c7d9f6359f77037.jpg",
                title = "今日发布啦",//"title": "今日发布啦",
                subTitle = "发布啦2222",// "subTitle": "发布啦2222",
                crateTime = 1543902358275,//"ctime": 1543902358275,
                type = 0,//"ntype": 0,
                url = "https://blog.csdn.net/feint123/article/details/77618298"//"ntype": 0,
        )
    }

    fun officialMessageList(): List<OfficialMessage> {
        val list = ArrayList<OfficialMessage>()
        for (i in 0..20) {
            list.add(officialMessage(i))
        }
        return list
    }

    fun reportReason(i: Int): ReportReason {
        return ReportReason(
                id = i.toLong(),
                name = "This is ReportReason!"
        )
    }

    fun reportReasonList(): List<ReportReason> {
        val list = ArrayList<ReportReason>()
        for (i in 0..20) {
            list.add(reportReason(i))
        }
        return list
    }

    fun authState(): AuthState {
        return AuthState(
                message = "您还未通过纯蜜官方认证！",// "msg": "您还未通过纯蜜官方认证！"
                score = "您还未通过纯蜜官方真人验证，靠谱值为0",//"score": "您还未通过纯蜜官方真人验证，靠谱值为0",
                /**
                 * 0 未认证  1 认证中  2 认证通过  3 认证未通过
                 */
                state = 0//"state": 0
        )
    }

    fun news(i: Int): News {
        return News(
                uuid = "m201901041009233f2607073100c31f",//"uuid": "m201901041009233f2607073100c31f",
                url = "https://blog.csdn.net/feint123/article/details/77618298",//"url": "https://api.chunmi69.com/news/news/m201901041009233f2607073100c31f",,,,
                title = "Kotlin运算符重载总结",// "title": "揭秘嫦娥四号月背之旅：攻克三大挑战终抵\"蟾宫后院\"",
                cover = MockPictures[i],//   "cover": "https://api.chunmi69.com/phoney/phoney/512/news/cover/1546567763964-0-842186299ed4562ee2ce07f4868e0f66.jpeg",
                type = 0,//"type": 1,,
                createTime = 1546567763952//"ctime": 1546567763952,
        )
    }

    fun newsList(): List<News> {
        val list = ArrayList<News>()
        for (i in 0..20) {
            list.add(news(i))
        }
        return list
    }

    fun photoApply(): PhotoApply {
        return PhotoApply(
                uid = "u20181204173309703e6c143d35a6a7",//"uuid": "u20181204173309703e6c143d35a6a7",
                uuid = "u20181204173309703e6c143d35a6a7",//"uuid": "u20181204173309703e6c143d35a6a7",
                nickname = "淞",//  "nickName": "淞",
                sex = 1,//"sex": 1,
                age = 20,//"age": null,
                height = 170,//"height": null,
                weight = 50.0,//"weight": null,
                cup = "E",//"cup": null
                /**
                 * 0 为审核  1 同意  2 拒绝  10 失效
                 */
                state = 1,//"state": 0,
                avatar = MockPictures.random(),//"portrait": "http://phoney.alance.pub/phoney/phoney/512/users/cover/6e1b51f15a51906faf2708fc9104cb9c.jpg",
                createTime = 0)
    }

    fun photoApplyList(): List<PhotoApply> {
        val list = ArrayList<PhotoApply>()
        for (i in 0..20) {
            list.add(photoApply())
        }
        return list
    }

    fun city(i: Int, j: Int): City {
        return City(
                id = j,
                provinceId = i,
                provinceName = "Province$i",
                name = "City$j"
        )
    }

    fun cityList(i: Int): List<City> {
        val list = ArrayList<City>()
        for (j in 0..20) {
            list.add(city(i, j))
        }
        return list
    }

    fun province(i: Int): Province {
        return Province(
                id = i,
                name = "Province$i",
                cities = cityList(i)
        )
    }

    fun provinceList(): List<Province> {
        val list = ArrayList<Province>()
        for (i in 0..20) {
            list.add(province(i))
        }
        return list
    }
}