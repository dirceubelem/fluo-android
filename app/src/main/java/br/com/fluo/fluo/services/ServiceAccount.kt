package br.com.fluo.fluo.services

import br.com.fluo.fluo.models.Account
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ServiceAccount {

    @POST("account/auth")
    fun auth(@Body account: Account) : Call<Account>

    @POST("account/forgot")
    fun forgot(@Body account: Account) : Call<Void>

    @POST("account")
    fun register(@Body account: Account) : Call<Account>

    @Multipart
    @POST("account/photo")
    fun sendPhoto(@Part image: MultipartBody.Part): Call<Account>

}