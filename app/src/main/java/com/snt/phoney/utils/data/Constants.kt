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

        const val USER_VEHICLES_CACHE_NAME: String = "user_vehicles_cache"
        const val USER_VEHICLES: String = "user_vehicles"
    }

    object Api {
        const val API_BASE_URL: String = "http://phoney.alance.pub"

        const val APP_ID = ""
        const val APP_ID_VALUE = ""
    }

}

