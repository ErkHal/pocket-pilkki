package com.android.erkhal.pocket_pilkki.remote

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface IGoogleAPIService {

    @GET("api/place/nearbysearch/json?")
    fun getLakes(@Query("location") location: String,
                         @Query("radius") radius: String,
                         @Query("type") type: String,
                         @Query("keyword") keyword: String,
                         @Query("key") key: String):
            Observable<Model.Response>


    companion object {
        fun create(): IGoogleAPIService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl("https://maps.googleapis.com/maps/")
                    .build()

            return retrofit.create(IGoogleAPIService::class.java)
        }
    }
}