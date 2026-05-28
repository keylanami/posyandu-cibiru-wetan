package com.desacibiruwetan.posyandu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.model.LoginData
import com.desacibiruwetan.posyandu.data.model.LoginRequest
import com.desacibiruwetan.posyandu.data.network.BaseResponse
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewmodel(private val repository: AuthRepository): ViewModel() {
    private val _loginState = MutableStateFlow<UiState<BaseResponse<LoginData>>>(UiState.Idle)
    val loginState: StateFlow<UiState<BaseResponse<LoginData>>> = _loginState.asStateFlow()

    fun login(email: String, password: String, deviceName: String) {
        viewModelScope.launch {
           val req = LoginRequest(email, password, deviceName)

            repository.login(req).collect { state ->
                _loginState.value = state

            }
        }
    }

}