package com.desacibiruwetan.posyandu.data.network

import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.model.AnggotaData
import com.desacibiruwetan.posyandu.data.model.AnggotaReq
import com.desacibiruwetan.posyandu.data.model.KeluargaData
import com.desacibiruwetan.posyandu.data.model.KeluargaReq
import com.desacibiruwetan.posyandu.data.model.LoginData
import com.desacibiruwetan.posyandu.data.model.LoginRequest
import com.desacibiruwetan.posyandu.data.model.RegisterRequest
import com.desacibiruwetan.posyandu.data.model.RumahData
import com.desacibiruwetan.posyandu.data.model.RumahRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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


    @GET("rumahs")
    suspend fun getAllRumah(
        @Header("Authorization") token: String
    ): Response<BaseResponse<List<RumahData>>>

    @GET("rumahs/{id}")
    suspend fun getRumahById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<BaseResponse<RumahData>>

    @POST("rumahs")
    suspend fun postRumah(
        @Header("Authorization") token: String,
        @Body request: RumahRequest
    ): Response<BaseResponse<RumahData>>

    @PUT("rumahs/{id}")
    suspend fun putRumah(
        @Header("Authorization") token: String,
        @Path("id") id: Int?,
        @Body request: RumahRequest
    ): Response<BaseResponse<RumahData>>


    @GET("keluargas")
    suspend fun getAllKeluarga(
        @Header("Authorization") token: String
    ): Response<BaseResponse<List<KeluargaData>>>


    @GET("keluargas/{id}")
    suspend fun getKeluargaById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<BaseResponse<KeluargaData>>

    @POST("rumahs/{rumahId}/keluargas")
    suspend fun postDataKeluarga(
        @Header("Authorization") token: String,
        @Path("rumahId") rumahId: Int,
        @Body request: KeluargaReq
    ): Response<BaseResponse<KeluargaData>>

    @PUT("keluargas/{id}")
    suspend fun putKeluarga(
        @Header("Authorization") token: String,
        @Path("id") id: Int?,
        @Body request: KeluargaReq
    ): Response<BaseResponse<KeluargaData>>



    @GET("anggotas/{id}")
    suspend fun getInfoAnggotaById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<BaseResponse<AnggotaData>>


    @PUT("anggotas/{id}")
    suspend fun putAnggotaId(
        @Header("Authorization") token: String,
        @Path("id") id: Int?,
        @Body request: AnggotaReq
    ): Response<BaseResponse<AnggotaData>>


    @POST("keluargas/{keluargaId}/anggotas")
    suspend fun postAnggota(
        @Header("Authorization") token: String,
        @Path("keluargaId") keluargaId: Int,
        @Body request: AnggotaReq
    ): Response<BaseResponse<AnggotaData>>

}