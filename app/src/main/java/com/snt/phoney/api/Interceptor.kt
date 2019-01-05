package com.snt.phoney.api

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.snt.phoney.BuildConfig
import com.snt.phoney.extensions.TAG
import com.snt.phoney.extensions.sendBroadcast
import com.snt.phoney.utils.data.MD5.md5
import okhttp3.*
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit


private val UTF8 = Charset.forName("UTF-8")
const val APP_SECRET = ""
const val ACTION_LOGIN_STATE_INVALID = "login_state_invalid"

@Suppress("unused")
fun Interceptor.isNotEmpty(param: CharSequence?): Boolean {
    return param != null && param.trim().isNotEmpty()
}

@Suppress("unused")
fun Interceptor.isEmpty(param: CharSequence?): Boolean {
    return param == null || param.trim().isEmpty()
}

@Suppress("unused")
fun Interceptor.getQueryParameters(httpUrl: HttpUrl): TreeMap<String, String> {
    val names = httpUrl.queryParameterNames()
    val params = TreeMap<String, String>()
    val iterator = names.iterator()
    while (iterator.hasNext()) {
        val name = iterator.next()
        val value = httpUrl.queryParameter(name)
        if (isNotEmpty(value)) {
            params[name] = value!!
        }
    }
    return params
}

fun Interceptor.getParameters(requestBody: RequestBody?): TreeMap<String, String> {
    when (requestBody) {
        is FormBody -> {
            val params = TreeMap<String, String>()
            for (i in 0..(requestBody.size() - 1)) {
                val value = requestBody.encodedValue(i)
                if (isNotEmpty(value)) {
                    params[requestBody.encodedName(i)] = value
                }
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

@Suppress("unused")
open class SignInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val method = request.method()
        return when (method) {
            "GET" -> chain.proceed(interceptGet(request))
            "POST" -> chain.proceed(interceptPost(request))
            else -> chain.proceed(request)
        }
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
                val bodyBuilder = FormBody.Builder(UTF8)
                val timestamp = System.currentTimeMillis().toString()
                val params = getParameters(body).apply {
                    put("appSecret", APP_SECRET)
                    put("timestamp", timestamp)
                }
                params["sign"] = getSignString(params)
                for ((name, value) in params) {
                    bodyBuilder.addEncoded(name, value)
                }
                return request.newBuilder().post(bodyBuilder.build()).build()
            }
            is MultipartBody -> {
                val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
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
            else -> return request
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
}

class NullOrEmptyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val method = request.method()
        return when (method) {
            "GET" -> chain.proceed(interceptGet(request))
            "POST" -> chain.proceed(interceptPost(request))
            else -> chain.proceed(request)
        }
    }


    private fun interceptGet(request: Request): Request {
        val httpUrl = request.url()
        val names = httpUrl.queryParameterNames()
        val urlBuilder = httpUrl.newBuilder()
        for (name in names) {
            val value = httpUrl.queryParameter(name)
            if (isEmpty(value)) {
                urlBuilder.removeAllQueryParameters(name)
            }
        }
        return request.newBuilder().url(urlBuilder.build()).build()
    }

    private fun interceptPost(request: Request): Request {
        val body = request.body()
        when (body) {
            is FormBody -> {
                val bodyBuilder = FormBody.Builder(UTF8)
                val params = getParameters(body)
                for ((name, value) in params) {
                    bodyBuilder.addEncoded(name, value)
                }
                return request.newBuilder().post(bodyBuilder.build()).build()
            }
            is MultipartBody -> {
                val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                val parts = body.parts()
                val iterator = parts.iterator()
                //移除掉 FormBody，保留其他类型 Body，比如文件类型
                while (iterator.hasNext()) {
                    val part = iterator.next()
                    if (part.body() !is FormBody) {
                        bodyBuilder.addPart(part)
                    }
                }
                //取出 FormBody 中的参数，过滤掉空值，然后重新加入
                val params = getParameters(body)
                for ((name, value) in params) {
                    if (isNotEmpty(value)) {
                        bodyBuilder.addPart(MultipartBody.Part.createFormData(URLEncoder.encode(name, UTF8.name()), URLEncoder.encode(value, UTF8.name())))
                    }
                }
                return request.newBuilder().post(bodyBuilder.build()).build()
            }
            else -> return request
        }
    }


}

class TimeoutInterceptor : Interceptor {

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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
        if (isNotEmpty(timeout)) {

            val time = Integer.valueOf(timeout)
            connectTimeout = time
            readTimeout = time
            writeTimeout = time
        } else {
            // @Headers("Connect-Timeout: 60000", "Read-Timeout: 60000", "Write-Timeout: 60000")
            val connectTime = request.header("Connect-Timeout")
            val readTime = request.header("Read-Timeout")
            val writeTime = request.header("Write-Timeout")
            if (isNotEmpty(connectTime)) {
                connectTimeout = Integer.valueOf(connectTime)
            }
            if (isNotEmpty(readTime)) {
                readTimeout = Integer.valueOf(readTime)
            }
            if (isNotEmpty(writeTime)) {
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

class LoginStateInterceptor(private val application: Application, private val gson: Gson) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val response = chain.proceed(originRequest)
        val source = response.body()?.source()
        source?.request(Long.MAX_VALUE)
        val buffer = source?.buffer()
        val result = buffer?.clone()?.readString(UTF8)
        Log.d("TTTT", "8888888888888888888888 res=$result")
        result?.let { result ->
            val jsonObject = gson.fromJson<JsonObject>(result, JsonObject::class.java)
            val code = jsonObject.getAsJsonObject("header").get("code").asInt
            Log.d("TTTT", "cccccccccccccccccccccccccc code=$code")
            if (code == 201 || code == 202) {
                Log.d("TTTT", "xxxxxxxxxxxxxxxxxxxxxxxxx code=$code")
                application.sendBroadcast(ACTION_LOGIN_STATE_INVALID)
            }
        }
        return response
    }

}
