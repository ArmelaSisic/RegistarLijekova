package com.example.registarlijekova

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("medicaments")
    fun getData(): Call<ArrayList<MedicamentDataItem>>

    @GET("categories")
    fun getData2(): Call<ArrayList<MedicamentsCategoryDataItem>>

    @GET("substances")
    fun getData3(
        @Query("drugid")
        id: Int?,
    ): Call<ArrayList<ActiveSubstanceItem>>

}