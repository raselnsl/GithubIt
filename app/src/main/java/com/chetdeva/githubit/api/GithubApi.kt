package com.chetdeva.githubit.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("search/users?sort=followers")
    fun searchUsers(@Query("q") query: String,
                    @Query("page") page: Int,
                    @Query("per_page") perPage: Int): Call<UsersSearchResponse>


    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GithubApi {
           /* val logger = HttpLoggingInterceptor()
            logger.level = Level.BODY

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()*/
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GithubApi::class.java)
        }
    }
}