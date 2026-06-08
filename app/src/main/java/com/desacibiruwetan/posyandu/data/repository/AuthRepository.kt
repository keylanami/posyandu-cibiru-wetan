package com.desacibiruwetan.posyandu.data.repository

import com.desacibiruwetan.posyandu.data.model.LoginData
import com.desacibiruwetan.posyandu.data.model.LoginRequest
import com.desacibiruwetan.posyandu.data.model.RegisterRequest
import com.desacibiruwetan.posyandu.data.network.ApiService
import com.desacibiruwetan.posyandu.data.network.BaseResponse
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.data.schema.UserSchema
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository(private val apiService: ApiService) {

    suspend fun login (request: LoginRequest): Flow<UiState<BaseResponse<LoginData>>> = flow {
        emit(UiState.Loading)

        try {
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                emit(UiState.Success(response.body()!!))
            } else {
                emit(UiState.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(UiState.Error("Tidak ada internet: ${e.localizedMessage}"))
        }
    }



    suspend fun register(request: RegisterRequest): Flow<UiState<BaseResponse<Any>>> = flow {
        emit(UiState.Loading)

        try {
            val response = apiService.register(request)

            if (response.isSuccessful && response.body() != null) {
                emit(UiState.Success(response.body()!!))

            } else {
                emit(UiState.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(UiState.Error("Tidak ada internet: ${e.localizedMessage}"))
        }
    }

    suspend fun getMe(token: String): Flow<UiState<BaseResponse<UserSchema>>> = flow { emit(UiState.Loading)
        try {

            val response = apiService.getMe(token)

            if (response.isSuccessful && response.body() != null) {
                emit(UiState.Success(response.body()!!))
            } else {
                emit(UiState.Error("Gagal mengambil data user: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(UiState.Error("Tidak ada internet: ${e.localizedMessage}"))
        }
    }



    suspend fun logout(token: String): Flow<UiState<BaseResponse<Any>>> = flow {
        emit(UiState.Loading)

        try {
            val response = apiService.logout("Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                emit(UiState.Success(response.body()!!))
            } else {
                emit(UiState.Error("Gagal Log Out" + response.message()))
            }
        } catch (e: Exception) {
            emit(UiState.Error("Tidak ada internet: ${e.localizedMessage}"))
        }

    }


}