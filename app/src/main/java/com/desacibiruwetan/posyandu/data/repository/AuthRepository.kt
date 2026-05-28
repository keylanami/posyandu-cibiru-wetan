package com.desacibiruwetan.posyandu.data.repository

import com.desacibiruwetan.posyandu.data.model.LoginData
import com.desacibiruwetan.posyandu.data.model.LoginRequest
import com.desacibiruwetan.posyandu.data.network.ApiService
import com.desacibiruwetan.posyandu.data.network.BaseResponse
import com.desacibiruwetan.posyandu.data.network.UiState
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
}