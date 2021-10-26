package com.example.currencyconverterapp

import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {
    @GET("eur.json")
    fun getPrices(): Call<PriceData?>?


}