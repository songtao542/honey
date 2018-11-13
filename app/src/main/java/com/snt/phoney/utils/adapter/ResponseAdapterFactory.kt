package com.snt.phoney.utils.adapter

import androidx.lifecycle.LiveData
import com.snt.phoney.domain.model.Response
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class ResponseAdapterFactory : Factory() {


    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        //todo
        return null
    }

}