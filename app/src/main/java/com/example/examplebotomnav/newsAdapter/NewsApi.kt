package com.example.examplebotomnav.newsAdapter

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("api/1/news?")
    fun getCurrentNewsData(@Query("country") country : String, @Query("apikey") apikey: String): Call<ResponseNews>
}