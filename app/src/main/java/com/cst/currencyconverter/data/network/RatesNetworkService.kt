package com.cst.currencyconverter.data.network

import retrofit2.Call
import retrofit2.http.GET

interface RatesNetworkService {
    @GET("latest")
    fun getRates(): Call<RatesRequestResult>
}
