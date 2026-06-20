package com.desacibiruwetan.posyandu.data.network

import com.desacibiruwetan.posyandu.data.model.AnggotaData
import com.desacibiruwetan.posyandu.data.model.AnggotaReq
import com.desacibiruwetan.posyandu.data.model.BalitaData
import com.desacibiruwetan.posyandu.data.model.BalitaReq
import com.desacibiruwetan.posyandu.data.model.BumilData
import com.desacibiruwetan.posyandu.data.model.BumilReq
import com.desacibiruwetan.posyandu.data.model.KeluargaData
import com.desacibiruwetan.posyandu.data.model.KeluargaOpt
import com.desacibiruwetan.posyandu.data.model.KeluargaReq
import com.desacibiruwetan.posyandu.data.model.LoginData
import com.desacibiruwetan.posyandu.data.model.LoginRequest
import com.desacibiruwetan.posyandu.data.model.RegisterRequest
import com.desacibiruwetan.posyandu.data.model.RumahData
import com.desacibiruwetan.posyandu.data.model.RumahRequest
import com.desacibiruwetan.posyandu.data.model.WusPusData
import com.desacibiruwetan.posyandu.data.model.WusPusReq
import com.desacibiruwetan.posyandu.data.schema.UserSchema
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
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

    @GET("user")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): Response<BaseResponse<UserSchema>>

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<BaseResponse<Any>>


    @GET("rumahs")
    suspend fun getAllRumah(
        @Header("Authorization") token: String,
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


    @GET("anggotas")
    suspend fun getAllAnggota(
        @Header("Authorization") token: String
    ): Response<BaseResponse<List<AnggotaData>>>


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


    @GET("anggotas/{anggotaId}/balita")
    suspend fun getBalitaById(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int
    ): Response<BaseResponse<BalitaData>>


    @PUT("anggotas/{anggotaId}/balita")
    suspend fun putBalita(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int,
        @Body request: BalitaReq
    ): Response<BaseResponse<Any>>


    @DELETE("anggotas/{anggotaId}/balita")
    suspend fun deleteBalita(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int
    ): Response<BaseResponse<Any>>


    @GET("balitas")
    suspend fun getAllBalita(
        @Header("Authorization") token: String
    ): Response<BaseResponse<List<AnggotaData>>>


    @POST("anggotas/{anggotaId}/balita")
    suspend fun postBalita(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int,
        @Body request: BalitaReq
    ): Response<BaseResponse<Any>>


    @POST("keluargas/{keluargaId}/anggotas")
    suspend fun postAnggota(
        @Header("Authorization") token: String,
        @Path("keluargaId") keluargaId: Int,
        @Body request: AnggotaReq
    ): Response<BaseResponse<AnggotaData>>


    @GET("keluargas/options")
    suspend fun getKeluargaOption(
        @Header("Authorization") token: String
    ): Response<BaseResponse<List<KeluargaOpt>>>


    @GET("bumils")
    suspend fun getAllBumil(
        @Header("Authorization") token: String
    ): Response<BaseResponse<List<BumilData>>>


    @POST("anggotas/{anggotaId}/bumils")
    suspend fun postbumil(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int,
        @Body request: BumilReq
    ): Response<BaseResponse<BumilData>>


    @GET("bumils/{id}")
    suspend fun getDetailBumilById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<BaseResponse<BumilData>>


    @PUT("bumils/{id}")
    suspend fun putBumil(
        @Header("Authorization") token: String,
        @Path("id") id: Int?,
        @Body request: BumilReq
    ): Response<BaseResponse<BumilData>>


    @DELETE("bumils/{id}")
    suspend fun deleteBumil(
        @Header("Authorization") token: String,
        @Path("bumilId") bumilId: Int
    ): Response<BaseResponse<Any>>


    @GET("wus-pus")
    suspend fun getAllWusPus(
        @Header("Authorization") token: String
    ): Response<BaseResponse<WusPusData>>


    @GET("anggotas/{anggotaId}/wus-pus")
    suspend fun getWusPusById(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int
    ): Response<BaseResponse<WusPusData>>


    @POST("anggotas/{anggotaId}/wus-pus")
    suspend fun postWusPus(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int,
        @Body request: WusPusReq
    ): Response<BaseResponse<WusPusData>>


    @PUT("anggotas/{anggotaId}/wus-pus")
    suspend fun putWusPus(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int,
        @Body request: WusPusReq
    ): Response<BaseResponse<WusPusData>>


    @PATCH("anggotas/{anggotaId}/wus-pus")
    suspend fun patchWusPus(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int,
        @Body request: WusPusReq
    ): Response<BaseResponse<WusPusData>>


    @DELETE("anggotas/{anggotaId}/wus-pus")
    suspend fun deleteWusPus(
        @Header("Authorization") token: String,
        @Path("anggotaId") anggotaId: Int
    ): Response<BaseResponse<Any>>


}