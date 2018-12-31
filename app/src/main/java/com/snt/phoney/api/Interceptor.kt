package com.snt.phoney.api

import android.text.TextUtils
import android.util.Log
import com.snt.phoney.BuildConfig
import com.snt.phoney.extensions.TAG
import com.snt.phoney.utils.data.MD5.md5
import okhttp3.*
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit


private val UTF8 = Charset.forName("UTF-8")
const val APP_SECRET = ""

@Suppress("unused")
open class SignInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val method = request.method()
        return when (method) {
            "GET" -> chain.proceed(interceptGet(request))
            "POST" -> chain.proceed(interceptPost(request))
            else -> chain.proceed(interceptDefault(request))
        }
    }

    private fun interceptDefault(request: Request): Request {
        return request
    }

    private fun interceptGet(request: Request): Request {
        val httpUrl = request.url()
        val timestamp = System.currentTimeMillis().toString()
        val params = getQueryParameters(httpUrl).apply {
            put("appSecret", APP_SECRET)
            put("timestamp", timestamp)
        }
        val newUrlBuilder = httpUrl.newBuilder()
                .addQueryParameter("timestamp", timestamp)
                .addQueryParameter("sign", getSignString(params))
        return request.newBuilder().url(newUrlBuilder.build()).build()
    }

    private fun interceptPost(request: Request): Request {
        val body = request.body()
        when (body) {
            is FormBody -> {
                val bodyBuilder = FormBody.Builder()
                val timestamp = System.currentTimeMillis().toString()
                val params = getParameters(body).apply {
                    put("appSecret", APP_SECRET)
                    put("timestamp", timestamp)
                }
                params["sign"] = getSignString(params)
                for ((name, value) in params) {
                    bodyBuilder.addEncoded(name, URLEncoder.encode(value, UTF8.name()))
                }
                return request.newBuilder().post(bodyBuilder.build()).build()
            }
            is MultipartBody -> {
                val bodyBuilder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                val parts = body.parts()
                val timestamp = System.currentTimeMillis().toString()
                val params = getParameters(body).apply {
                    put("appSecret", APP_SECRET)
                    put("timestamp", timestamp)
                }
                parts.add(MultipartBody.Part.createFormData("timestamp", timestamp))
                parts.add(MultipartBody.Part.createFormData("sign", getSignString(params)))
                for (part in parts) {
                    bodyBuilder.addPart(part)
                }
                return request.newBuilder().post(bodyBuilder.build()).build()
            }
            else -> return interceptDefault(request)
        }
    }

    private fun getSignString(params: TreeMap<String, String>): String {
        val paramString = StringBuilder()
        val sorted = params.toSortedMap(Comparator { o1, o2 -> o1.toUpperCase().compareTo(o2.toUpperCase()) })
        for ((name, value) in sorted) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "to sign param:($name=$value)")
            }
            paramString.append(value)
        }
        return md5(paramString.toString().toUpperCase()).toUpperCase()
    }

    private fun getQueryParameters(httpUrl: HttpUrl): TreeMap<String, String> {
        val nameSet = httpUrl.queryParameterNames()
        val params = TreeMap<String, String>()
        for (name in nameSet) {
            val value = httpUrl.queryParameter(name)
            value?.let {
                params[name] = it
            }
        }
        return params
    }

    private fun getParameters(requestBody: RequestBody?): TreeMap<String, String> {
        when (requestBody) {
            is FormBody -> {
                val params = TreeMap<String, String>()
                for (i in 0..(requestBody.size() - 1)) {
                    params[requestBody.encodedName(i)] = requestBody.encodedValue(i)
                }
                return params
            }
            is MultipartBody -> {
                val parts = requestBody.parts()
                val params = TreeMap<String, String>()
                for (part in parts) {
                    params.putAll(getParameters(part.body()))
                }
                return params
            }
            else -> return TreeMap()
        }
    }
}

class TimeoutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var connectTimeout = chain.connectTimeoutMillis()
        var readTimeout = chain.readTimeoutMillis()
        var writeTimeout = chain.writeTimeoutMillis()

        if (BuildConfig.DEBUG) {
            Log.d("TimeoutInterceptor", "original connectTimeout=$connectTimeout")
            Log.d("TimeoutInterceptor", "original readTimeout=$readTimeout")
            Log.d("TimeoutInterceptor", "original writeTimeout=$writeTimeout")
        }

        //@Headers("Timeout: 60000")
        val timeout = request.header("Timeout")
        if (!TextUtils.isEmpty(timeout)) {
            val time = Integer.valueOf(timeout)
            connectTimeout = time
            readTimeout = time
            writeTimeout = time
        } else {
            // @Headers("Connect-Timeout: 60000", "Read-Timeout: 60000", "Write-Timeout: 60000")
            val connectTime = request.header("Connect-Timeout")
            val readTime = request.header("Read-Timeout")
            val writeTime = request.header("Write-Timeout")
            if (!TextUtils.isEmpty(connectTime)) {
                connectTimeout = Integer.valueOf(connectTime)
            }
            if (!TextUtils.isEmpty(readTime)) {
                readTimeout = Integer.valueOf(readTime)
            }
            if (!TextUtils.isEmpty(writeTime)) {
                writeTimeout = Integer.valueOf(writeTime)
            }
        }
        if (BuildConfig.DEBUG) {
            Log.d("TimeoutInterceptor", "config connectTimeout=$connectTimeout")
            Log.d("TimeoutInterceptor", "config readTimeout=$readTimeout")
            Log.d("TimeoutInterceptor", "config writeTimeout=$writeTimeout")
        }
        return chain
                .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .proceed(request)
    }

}

//
//class LoginStateInterceptor @Inject constructor(private val userRepository: UserRepository) : SignInterceptor() {
//
//    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//        val originRequest = chain.request()
//        val response = chain.proceed(originRequest)
//        val source = response.body()?.source()
//        source?.request(Long.MAX_VALUE)
//        val buffer = source?.buffer()
//        val res = buffer?.clone()?.readString(UTF8)
//        res?.let { res ->
//            val result = Gson().fromJson<com.snt.phoney.domain.model.Response<Any>>(res, com.snt.phoney.domain.model.Response::class.java)
//            if (result?.code == 202) {
//                val user = userRepository?.user
//                user?.let {
//                    val builder = Request.Builder()
//                    builder.url("${Constants.Api.BASE_URL}/OpenApi/UserLogin.aspx")
//                    var bodyBuilder = FormBody.Builder()
//                    bodyBuilder.addEncoded("username", URLEncoder.encode(it.phone, UTF8.name()))
//                    bodyBuilder.addEncoded("userpwd", URLEncoder.encode(it.password, UTF8.name()))
//                    bodyBuilder.addEncoded("RegistrationId", URLEncoder.encode(it.registrationId, UTF8.name()))
//                    //bodyBuilder.addEncoded("RegistrationId", URLEncoder.encode("e7412ef50b78985bc4352975606c05d4",  UTF8.name()))
//                    builder.post(bodyBuilder.build())
//                    //val loginResponse = Api.getOkHttpClient().newCall(builder.build()).execute()
//                    val loginResponse = chain.proceed(interceptPost(builder.build()))
//                    val loginRes = loginResponse?.body()?.source()?.readString(UTF8)
//                    if (loginRes != null) {
//                        val loginResult =
//                                Gson().fromJson<com.snt.phoney.domain.model.Response<User>>(loginRes, object : TypeToken<com.snt.phoney.domain.model.Response<User>>() {}.type)
//
//                        if (loginResult?.code == 1) {
//                            loginResult?.data?.id?.let { key ->
//                                user.id = key
//                                userRepository?.user = user
//                                Log.d(TAG, "retry origin request with new user: $user")
//                                return chain.proceed(interceptPost(newRequest(key, originRequest)))
//                                //return Api.getOkHttpClient().newCall(newRequest(originRequest)).execute()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return response
//    }
//
//    private fun updateRequest(key: String, request: Request): Request {
//        var method = request.method()
//        val builder = Request.Builder()
//        return when (method) {
//            "GET" -> {
//                val urlBuilder = request.url().newBuilder()
//                if (request.url().queryParameter("UserKey") != null) {
//                    urlBuilder.removeAllQueryParameters("UserKey")
//                            .addQueryParameter("UserKey", key)
//                }
//                builder.url(urlBuilder.build()).build()
//            }
//            "POST" -> {
//                val params = getParameters(request?.body())
//                builder.url(request.url())
//                var bodyBuilder = FormBody.Builder()
//                for ((name, _) in params) {
//                    if (name == "UserKey") {
//                        bodyBuilder.addEncoded(name, URLEncoder.encode(key, UTF8.name()))
//                        break
//                    }
//                }
//                builder.post(bodyBuilder.build())
//                builder.build()
//            }
//            else -> request
//        }
//    }
//
//    private fun newRequest(key: Int, request: Request): Request {
//        var method = request.method()
//        val builder = Request.Builder()
//        return when (method) {
//            "GET" -> {
//                val urlBuilder = request.url().newBuilder()
//                        .removeAllQueryParameters("sign")
//                        .removeAllQueryParameters("appid")
//                        .removeAllQueryParameters("requestdate")
//
//                if (request.url().queryParameter("UserKey") != null) {
//                    urlBuilder.removeAllQueryParameters("UserKey")
//                            .addQueryParameter("UserKey", key.toString())
//                }
//                builder.url(urlBuilder.build()).build()
//            }
//            "POST" -> {
//                val params = getParameters(request?.body())
//                builder.url(request.url())
//                var bodyBuilder = FormBody.Builder()
//                for ((name, value) in params) {
//                    if (name == "sign" || name == "appid" || name == "requestdate") {
//                        continue
//                    }
//                    if (name == "UserKey") {
//                        bodyBuilder.addEncoded(name, URLEncoder.encode(key, UTF8.name()))
//                    } else {
//                        bodyBuilder.addEncoded(name, URLEncoder.encode(value, UTF8.name()))
//                    }
//                }
//                builder.post(bodyBuilder.build())
//                builder.build()
//            }
//            else -> request
//        }
//
//    }
//
//
//}




