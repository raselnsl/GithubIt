package com.chetdeva.githubit.api

import com.chetdeva.githubit.model.StationLeaveHistoryResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StationLeaveApi {

    @GET("leave/api/stations")
    fun searchUsers(
            @Header("Authorization") token: String,
            @Query("page") page: Int
    ): Call<StationLeaveHistoryResponse>

    companion object {
        private const val BASE_URL = "http://www.bsmrmu.edu.bd/"

        fun create(): StationLeaveApi {
            /* val logger = HttpLoggingInterceptor()
             logger.level = Level.BODY

             val client = OkHttpClient.Builder()
                     .addInterceptor(logger)
                     .build()*/
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(StationLeaveApi::class.java)
        }
    }
}