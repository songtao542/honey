package com.snt.phoney.utils.data

object Constants {
    object Extra {
        const val TOKEN = "token"
        const val USER = "user"
        const val USER_ID = "user_id"
        const val PERMISSION = "permission"
        const val LIST = "list"
        const val URL_LIST = "url_list"
        const val URI_LIST = "uri_list"
        const val PHOTO_LIST = "photo_list"
        const val PHOTO = "photo"
        const val TITLE = "title"
        const val URL = "url"
        const val URI = "uri"
        const val INDEX = "index"
        const val DELETABLE = "deletable"
        const val PASSWORD = "password"
        const val UUID = "uuid"
        const val DATA = "data"
        const val TYPE = "type"

        const val CALLER = "caller"
        const val CALLEE = "callee"
        const val MODE = "mode"
        const val IS_MEMBER = "is_member"
        const val STATE = "state"

        const val THEME = "theme"
        const val ARGUMENT = "argument"
        const val PAGE = "page"

        const val THEME_OPEN_SWIPEBACK = "open_swipeback"
    }

    object Preference {
        const val USER_EMAIL: String = "email"
        const val DISTANCE_IN_METRIC: String = "distance_in_metric"
        const val TEMPERATURE_IN_METRIC: String = "temperature_in_metric"
        const val LOGIN_COUNTRY: String = "login_country"
        const val LOGIN_ERROR_STATE_NAME: String = "login_error_state_name"
        const val LOGIN_ERROR_STATE_TIME: String = "login_error_state_time"
    }

    object Cache {
        const val USER = "user"

        const val WX_ACCESS_TOKEN = "wx_access_token"

        const val WX_USER = "wx_user"
        const val WEIBO_USER = "wx_user"
        const val QQ_USER = "wx_user"
    }

    object Api {
        const val DOMAIN_NAME = "api.chunmi69.com"
        const val BASE_URL = "https://$DOMAIN_NAME"
        const val DOWNLOAD_URL = "$BASE_URL/web/html/download/index.html"
        const val USER_PROTOCOL_URL = "$BASE_URL/message/agreement"
        const val HELP_URL = "$BASE_URL/message/help"
    }

    object Bugly {
        const val APP_ID = "f2ff7f1586"
        const val APP_KEY = "78f0631f-81b8-4bc1-94be-983ecb43c0d6"
    }

    //https://openauth.alipaydev.com/oauth2/appToAppAuth.htm?app_id=2016092400585139&redirect_uri=https://api.chunmi69.com/auth/alipay/getUserInfo?state=用户token
    object Tencent {
        const val APP_ID = "1107903594"
        const val APP_KEY = "Jzgzv5j0ro5RWVUP"
    }

    object Wechat {
        const val APP_ID = "wxa876e58bf61e9dac"
        const val APP_SECRET = "11b4b2933148e7b9e07562b5a6b5b16b"
        const val BASE_URL = "https://api.weixin.qq.com"
    }

    object Weibo {
        const val APP_KEY = "2202388875"
        const val APP_SECRET = "055a2833b9cc9d6f0c5de0550d861018"
        const val BASE_URL = "https://api.weibo.com"
        //const val REDIRECT_URL = "https://api.weibo.com/oauth2/default.html"
        const val REDIRECT_URL = "http://www.weibo.com"
        const val SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"
    }

    object UMeng {
        const val APP_KEY = "5be8e7eaf1f556b885000206"
    }

    object AMap {
        const val APP_KEY = "23a40ad949d9ab5a652d4920642ddb19"
    }

    object JPush {
        const val APP_KEY = "eacade5d281c93bfb55b59d3"
        const val APP_SECRET = "beff4196dff2f2492ab7e429"
    }
}

