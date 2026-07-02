    package com.desacibiruwetan.posyandu.data.repository
    
    import com.desacibiruwetan.posyandu.data.model.LoginData
    import com.desacibiruwetan.posyandu.data.model.LoginRequest
    import com.desacibiruwetan.posyandu.data.model.RegisterRequest
    import com.desacibiruwetan.posyandu.data.network.ApiService
    import com.desacibiruwetan.posyandu.data.network.BaseResponse
    import com.desacibiruwetan.posyandu.data.network.UiState
    import com.desacibiruwetan.posyandu.data.schema.UserSchema
    import com.desacibiruwetan.posyandu.utils.SessionManager
    import com.squareup.moshi.Moshi
    import com.squareup.moshi.Types
    import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.flow
    import retrofit2.Response
    
    class AuthRepository(private val apiService: ApiService) {
    
        suspend fun login (request: LoginRequest): Flow<UiState<BaseResponse<LoginData>>> = flow {
            emit(UiState.Loading)
    
            try {
                val response = apiService.login(request)
    
                if (response.isSuccessful && response.body() != null) {
                    emit(UiState.Success(response.body()!!))
                } else {
                    emit(UiState.Error(parseError(response)))
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
                    emit(UiState.Error(parseError(response)))
                }
            } catch (e: Exception) {
                emit(UiState.Error("Tidak ada internet: ${e.localizedMessage}"))
            }
        }
    
        suspend fun getMe(token: String): Flow<UiState<BaseResponse<UserSchema>>> = flow { emit(UiState.Loading)
            try {
    
                val response = apiService.getMe(SessionManager.formatAuthorizationHeader(token))
    
                if (response.isSuccessful && response.body() != null) {
                    emit(UiState.Success(response.body()!!))
                } else if (response.code() == 401) {
                    emit(UiState.Error("Unauthorized"))
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
                val response = apiService.logout(SessionManager.formatAuthorizationHeader(token))
    
                if (response.isSuccessful && response.body() != null) {
                    emit(UiState.Success(response.body()!!))
                } else {
                    emit(UiState.Error(parseError(response, "Gagal Log Out")))
                }
            } catch (e: Exception) {
                emit(UiState.Error("Tidak ada internet: ${e.localizedMessage}"))
            }
    
        }

        private fun parseError(response: Response<*>, defaultMessage: String = "Terjadi kesalahan"): String {
            val errorBody = response.errorBody()?.string()
            if (errorBody.isNullOrBlank()) return response.message().ifBlank { defaultMessage }

            return try {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val type = Types.newParameterizedType(BaseResponse::class.java, Any::class.java)
                val adapter = moshi.adapter<BaseResponse<Any>>(type)
                val errorResponse = adapter.fromJson(errorBody)

                val errors = errorResponse?.errors as? Map<*, *>
                val firstErrorMessage = errors?.values?.filterIsInstance<List<*>>()?.firstOrNull()?.firstOrNull()?.toString()
                    ?: errors?.values?.firstOrNull()?.toString()

                firstErrorMessage ?: errorResponse?.message ?: response.message().ifBlank { defaultMessage }
            } catch (e: Exception) {
                response.message().ifBlank { defaultMessage }
            }
        }
    
    
    }
