package br.com.fluo.fluo.services

import br.com.fluo.fluo.app.FluoApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInitializer {

    companion object {

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                    FluoApp.account?.let {
                        newRequest.addHeader("token", it.token)
                    }
                    chain.proceed(newRequest.build())
                }
                .addInterceptor(HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                })
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
        }

    }

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(FluoApp.URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun serviceAccount(): ServiceAccount {
        return retrofit.create(ServiceAccount::class.java)
    }

    fun serviceProject(): ServiceProject {
        return retrofit.create(ServiceProject::class.java)
    }

}