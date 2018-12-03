package com.snt.phoney.api

import java.nio.charset.Charset

private val UTF8 = Charset.forName("UTF-8")

//open class SignInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//        var request = chain.request()
//        var method = request.method()
//        return when (method) {
//            "GET" -> chain.proceed(interceptGet(request))
//            "POST" -> chain.proceed(interceptPost(request))
//            else -> chain.proceed(interceptDefault(request))
//        }
//    }
//
//    protected fun interceptDefault(request: Request): Request {
//        var httpUrl = request.url()
//        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        val requestdate = format.format(Date())
//        val params = getQueryParameters(httpUrl)
//        params["appid"] = APP_ID
//        params["requestdate"] = requestdate
//        val newUrlBuilder = httpUrl.newBuilder()
//                .addQueryParameter("appid", APP_ID)
//                .addQueryParameter("requestdate", requestdate)
//                .addQueryParameter("sign", getSignString(params))
//        return request.newBuilder().url(newUrlBuilder.build()).build()
//    }
//
//    protected fun interceptGet(request: Request): Request {
//        return interceptDefault(request)
//    }
//
//    protected fun interceptPost(request: Request): Request {
//        var body = request.body()
//        when (body) {
//            is FormBody -> {
//                var bodyBuilder = FormBody.Builder()
//                var params = getParameters(body)
//                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                params["appid"] = APP_ID
//                params["requestdate"] = format.format(Date())
//                params["sign"] = getSignString(params)
//                for ((name, value) in params) {
//                    bodyBuilder.addEncoded(name, URLEncoder.encode(value, UTF8.name()))
//                }
//                return request.newBuilder().post(bodyBuilder.build()).build()
//            }
//            is MultipartBody -> {
//                var bodyBuilder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
//                var params = getParameters(body)
//                var parts = body.parts()
//                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                val requestdate = format.format(Date())
//                params["appid"] = APP_ID
//                params["requestdate"] = requestdate
//                parts.add(MultipartBody.Part.createFormData("appid", APP_ID))
//                parts.add(MultipartBody.Part.createFormData("requestdate", requestdate))
//                parts.add(MultipartBody.Part.createFormData("sign", getSignString(params)))
//                for (part in parts) {
//                    bodyBuilder.addPart(part)
//                }
//                return request.newBuilder().post(bodyBuilder.build()).build();
//            }
//            else -> return interceptDefault(request)
//        }
//    }
//
//    private fun getSignString(params: TreeMap<String, String>): String {
//        val paramString = StringBuilder()
//        val sorted = params.toSortedMap(Comparator { o1, o2 -> o1.toUpperCase().compareTo(o2.toUpperCase()) })
//        for ((name, value) in sorted) {
//            if (BuildConfig.DEBUG) {
//                Log.d(TAG, "to sign param:($name=$value)")
//            }
//            paramString.append(name).append("=").append(value).append("&")
//        }
//        paramString.append("KEY=$APP_KEY")
//        return md5(paramString.toString().toUpperCase()).toUpperCase()
//    }
//
//    private fun getQueryParameters(httpUrl: HttpUrl): TreeMap<String, String> {
//        var nameSet = httpUrl.queryParameterNames()
//        var params = TreeMap<String, String>()
//        for (name in nameSet) {
//            val value = httpUrl.queryParameter(name)
//            value?.let {
//                params[name] = it
//            }
//        }
//        return params
//    }
//
//    protected fun getParameters(requestBody: RequestBody?): TreeMap<String, String> {
//        when (requestBody) {
//            is FormBody -> {
//                var formBody: FormBody = requestBody
//                var params = TreeMap<String, String>()
//                for (i in 0..(formBody.size() - 1)) {
//                    params[formBody.encodedName(i)] = formBody.encodedValue(i)
//                }
//                return params
//            }
//            is MultipartBody -> {
//                var multipartBody: MultipartBody = requestBody
//                var parts = multipartBody.parts()
//
//                var params = TreeMap<String, String>()
//                for (part in parts) {
//                    params.putAll(getParameters(part.body()))
//                }
//                return params
//            }
//            else -> return TreeMap()
//        }
//    }
//
//
//    private fun md5(string: String): String {
//        if (BuildConfig.DEBUG) {
//            Log.d(TAG, "to sign string: $string")
//        }
//        try {
//            //获取md5加密对象
//            val instance: MessageDigest = MessageDigest.getInstance("MD5")
//            //对字符串加密，返回字节数组
//            val digest: ByteArray = instance.digest(string.toByteArray())
//            var sb = StringBuilder()
//            for (b in digest) {
//                //获取低八位有效值
//                var i: Int = b.toInt() and 0xff
//                //将整数转化为16进制
//                var hexString = Integer.toHexString(i)
//                if (hexString.length < 2) {
//                    //如果是一位的话，补0
//                    hexString = "0$hexString"
//                }
//                sb.append(hexString)
//            }
//            val sign = sb.toString()
//            if (BuildConfig.DEBUG) {
//                Log.d(TAG, "signed string : $sign")
//            }
//            return sign
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        }
//        return ""
//    }
//
//}
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




