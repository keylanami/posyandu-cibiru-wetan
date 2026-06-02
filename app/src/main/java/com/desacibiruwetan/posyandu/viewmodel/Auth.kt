package com.desacibiruwetan.posyandu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.model.LoginData
import com.desacibiruwetan.posyandu.data.model.LoginRequest
import com.desacibiruwetan.posyandu.data.model.RegisterRequest
import com.desacibiruwetan.posyandu.data.network.BaseResponse
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.data.repository.AuthRepository
import com.desacibiruwetan.posyandu.data.schema.UserSchema
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewmodel(private val repository: AuthRepository): ViewModel() {
    private val _loginState = MutableStateFlow<UiState<BaseResponse<LoginData>>>(UiState.Idle)
    val loginState: StateFlow<UiState<BaseResponse<LoginData>>> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<BaseResponse<Any>>>(UiState.Idle)
    val registerState: StateFlow<UiState<BaseResponse<Any>>> = _registerState.asStateFlow()
    private val _getMeState = MutableStateFlow<UiState<BaseResponse<UserSchema>>>(UiState.Idle)

    private val _logoutState = MutableStateFlow<UiState<BaseResponse<Any>>>(UiState.Idle)
    val logoutState: StateFlow<UiState<BaseResponse<Any>>> = _logoutState.asStateFlow()



    fun login(email: String, password: String, deviceName: String) {
        viewModelScope.launch {
           val req = LoginRequest(email, password, deviceName)

            repository.login(req).collect { state ->
                _loginState.value = state

            }
        }
    }

    fun resetLoginState(){
        _loginState.value = UiState.Idle
    }

    fun register(request: RegisterRequest){
        viewModelScope.launch {
            repository.register(request).collect { state ->
                _registerState.value = state
            }
        }
    }

    fun resetRegisterState(){
        _registerState.value = UiState.Idle
    }



    val getMeState: StateFlow<UiState<BaseResponse<UserSchema>>> = _getMeState.asStateFlow()

    fun getMe(token: String) {
        viewModelScope.launch {
            repository.getMe(token).collect { state ->
                _getMeState.value = state
            }
        }
    }

    fun resetGetMeState() {
        _getMeState.value = UiState.Idle
    }


    fun logout(token: String) {
        viewModelScope.launch {
            repository.logout(token).collect { state ->
                _logoutState.value = state
            }
        }
    }

    fun resetLogoutState(){
        _logoutState.value = UiState.Idle
    }

}