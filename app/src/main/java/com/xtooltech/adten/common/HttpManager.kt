package com.xtooltech.adten.common

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates

class HttpManager private  constructor(){

    private lateinit var apiService: ApiService
    private lateinit var retrofit:Retrofit

    companion object{
        private  var instance:HttpManager?=null
            get() {
                if (field == null) {
                    field = HttpManager()
                }
                return field
            }

        fun get():HttpManager{
            return instance!!
        }
    }

    fun init(){
        retrofit = Retrofit.Builder()
            .baseUrl("http://19.87.4.145:8112")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

         apiService = retrofit?.create(ApiService::class.java)
    }

    fun getService():ApiService{
        return apiService
    }

}