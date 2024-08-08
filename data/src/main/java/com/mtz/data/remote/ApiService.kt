package com.mtz.data.remote

import com.mtz.data.MockJson
import com.mtz.data.remote.dto.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //remove mock for real api
    //should store api key in secrets.properties
    @MockJson("live.json")
    @GET("live")
    suspend fun getExchangeRates(
        @Query("source") source: String,
        @Query("access_key") apiKey: String = "38be745f2952c0d34980c5b58ab7a3fd",
    ): ExchangeRateResponse
}
