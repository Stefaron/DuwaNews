package com.example.examplebotomnav.newsAdapter

import com.example.examplebotomnav.detailNews.DetailResponse
import com.example.examplebotomnav.kategori.responseKategori.SportResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("news")
    fun getCurrentNewsData(@Query("apikey") apikey: String, @Query("country") country : String, @Query("category") category : String): Call<ResponseNews>

    @GET("news")
    fun getCategoryNewsData(@Query("apikey") apikey: String, @Query("country") country : String, @Query("category") category : String): Call<SportResponse>

    @GET("news")
    fun getDetailNewsData(@Query("apikey") apikey: String, @Query("id") id : String): Call<DetailResponse>

    @GET("news")
    fun searchNews(@Query("apikey") apikey: String, @Query("q") q : String, @Query("country") country : String): Call<ResponseNews>

}