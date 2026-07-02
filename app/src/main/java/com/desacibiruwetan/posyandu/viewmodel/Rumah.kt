package com.desacibiruwetan.posyandu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.repository.RumahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RumahViewmodel(private val repository: RumahRepository): ViewModel(){

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing = _isSyncing.asStateFlow()

    val listRumahLocal: StateFlow<List<RumahEntity>> = repository.getAllRumahLocal().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue =  emptyList()
    )

    fun syncDataRumah(token: String){
        viewModelScope.launch {
            _isSyncing.value = true
            try {
                repository.pullDataFromServer(token)
            } finally {
                _isSyncing.value = false
            }
        }
    }


    fun tambahRumah(token: String, alamat: String, rtId: Int, onSuccess: (Int) -> Unit){
        viewModelScope.launch {
            val newId = repository.addNewRumah(token, alamat, rtId)
            onSuccess(newId.toInt())
        }
    }

    fun updateRumah(token: String, rumahLocal: RumahEntity, alamatBaru: String){
        viewModelScope.launch {
            repository.updateRumah(token, rumahLocal, alamatBaru)
        }
    }
}
