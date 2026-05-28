package com.desacibiruwetan.posyandu.data.network

import com.desacibiruwetan.posyandu.data.model.LoginData
import com.desacibiruwetan.posyandu.data.model.LoginRequest
import com.desacibiruwetan.posyandu.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<BaseResponse<LoginData>>


    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<BaseResponse<Any>>


    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<BaseResponse<Any>>



//    @POST("rumahs")
//    suspend fun postRumah(
//        @Header("Authorization") token: String,
//        @Body request: RumahRequest
//    ): Response<BaseResponse<RumahResponse>>
}