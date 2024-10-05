package com.example.work5.data.network

import com.example.work5.data.model.Cat
import retrofit2.Call
import retrofit2.http.GET

interface CatApi {
    @GET("v1/images/search?limit=1&category_ids=5")
    fun getCat(): Call<List<Cat>>
}