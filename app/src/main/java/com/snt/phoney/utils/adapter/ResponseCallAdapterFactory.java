package com.snt.phoney.utils.adapter;

import android.util.Log;

import com.snt.phoney.domain.model.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class ResponseCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Log.d("TTTT", "fffffffffff returnType=" + returnType);
        Class<?> rawType = getRawType(returnType);
        if (rawType == Response.class) {
            Type type = getParameterUpperBound(0, (ParameterizedType) returnType);
            Class<?> rType = getRawType(type);
            Log.d("TTTT", "fffffffffff type=" + type);
            Log.d("TTTT", "fffffffffff rType=" + rType);
            return new ResponseCallAdapter(type);
        }
        return null;
    }


    class ResponseCallAdapter<R> implements CallAdapter<R, Response<R>> {

        private Type returnType;

        public ResponseCallAdapter(Type returnType) {
            this.returnType = returnType;
        }

        @Override
        public Type responseType() {
            return returnType;
        }

        @Override
        public Response<R> adapt(Call<R> call) {
            try {

                Log.d("TTTT", "fffffffffff adapt=" + call.getClass());
                Log.d("TTTT", "fffffffffff adapt=" + getRawType(call.getClass()));

                //return new Response<>( call.execute());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
