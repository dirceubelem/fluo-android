package br.com.fluo.fluo.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ServiceDatabase {

    @GET("database")
    fun getDatabase(): Call<ResponseBody>

}