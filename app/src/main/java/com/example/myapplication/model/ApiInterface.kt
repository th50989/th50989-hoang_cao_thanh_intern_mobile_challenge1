package com.example.myapplication.model

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
        .baseUrl("https://v6.exchangerate-api.com/v6/d1df7b149ae19f8bdd541f2d/") // I trust to give my key
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ExchangeRateApi::class.java)

    suspend fun fetchExchangeRate(from: String,to: String,amount: Double): ExchangeRateResponse = api.getExchangeRate(from,to,amount)
}