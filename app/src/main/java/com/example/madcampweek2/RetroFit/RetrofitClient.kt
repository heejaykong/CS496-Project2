package com.example.madcampweek2.RetroFit

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*


class RetrofitClient private constructor() {
    val apiService: ApiService

    companion object {
        val instance = RetrofitClient()
    }

    init {
        val client = OkHttpClient.Builder().build()
        apiService = Retrofit.Builder()
            .baseUrl("http://192.249.18.208:3000")
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("/contacts/get/{id}")
    fun contactsGet(
        @Path("id") id: String?
    ) : Call <ResponseBody?>?

    @POST("/contacts/post/{name}/{phoneNum}")
    fun contactsPost(
        @Path("name") name: String?,
        @Path("phoneNum") phoneNum: String?
    ): Call<ResponseBody?>?

    @PUT("/contacts/put/{id}/{name}/{phoneNum}")
    fun contactsPut(
        @Path("id") id : String?,
        @Path("name") name: String?,
        @Path("phoneNum") phoneNum: String?
    ): Call<ResponseBody?>?

    @DELETE("/contacts/delete/{id}")
    fun contactsDelete(
        @Path("id") id: String?
    ) : Call <ResponseBody?>?
}