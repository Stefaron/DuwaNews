package com.example.examplebotomnav.newsAdapter

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("news")
    fun getCurrentNewsData(@Query("apikey") apikey: String, @Query("country") country : String): Call<ResponseNews>
}