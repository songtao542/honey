/*
 *
 * Copyright (C) 2018. Faraday&Future
 * All rights reserved.
 * PROPRIETARY AND CONFIDENTIAL.
 * NOTICE: All information contained herein is, and remains the property of Faraday&Future Inc.
 * The intellectual and technical concepts contained herein are proprietary to Faraday&Future Inc.
 * and may be covered by U.S. and Foreign Patents, patents in process, and are protected
 * by trade secret and copyright law. Dissemination of this code or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Faraday&Future Inc.
 * Access to the source code contained herein is hereby forbidden to anyone except current
 * Faraday&Future Inc. employees or others who have executed Confidentiality and
 *  Non-disclosure agreements covering such access.
 *
 *
 */

package com.snt.phoney.utils.data

object Constants {
    object Preference {
        const val USER_EMAIL: String = "email"
        const val DISTANCE_IN_METRIC: String = "distance_in_metric"
        const val TEMPERATURE_IN_METRIC: String = "temperature_in_metric"
        const val LOGIN_COUNTRY: String = "login_country"
        const val LOGIN_ERROR_STATE_NAME: String = "login_error_state_name"
        const val LOGIN_ERROR_STATE_TIME: String = "login_error_state_time"
    }

    object Cache {
        const val USER_CACHE_NAME: String = "user_cache"
        const val USER: String = "user"
    }

    object Api {
        const val BASE_URL: String = "http://phoney.alance.pub"
    }

    object Tencent {
        const val APP_ID = "1107903594"
        const val APP_KEY = "Jzgzv5j0ro5RWVUP"
    }

    object Wechat {
        const val APP_ID = "wxa876e58bf61e9dac"
        const val APP_SECRET = "11b4b2933148e7b9e07562b5a6b5b16b"
    }

    object Weibo {
        const val APP_KEY = "2202388875"
        const val APP_SECRET = "055a2833b9cc9d6f0c5de0550d861018"
        const val BASE_URL: String = "https://api.weibo.com"
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

