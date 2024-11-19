package com.example.myapplication.network

import com.example.myapplication.model.ExchangeRateResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("pair/{from}/{to}/{amount}")
    suspend fun getExchangeRate(
        @Path("from") from:String,
        @Path("to") to:String,
        @Path("amount") amount:Double): ExchangeRateResponse
}

class ExchangeRateRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://v6.exchangerate-api.com/v6/{your key here since i cant publish it on github public}/") // I trust to give my key
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ExchangeRateApi::class.java)

    suspend fun fetchExchangeRate(from: String,to: String,amount: Double): ExchangeRateResponse = api.getExchangeRate(from,to,amount)
}