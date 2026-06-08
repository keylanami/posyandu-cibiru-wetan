package com.desacibiruwetan.posyandu.viewmodel

import android.service.notification.Condition.newId
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.repository.RumahRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RumahViewmodel(private val repository: RumahRepository): ViewModel(){

    val listRumahLocal: StateFlow<List<RumahEntity>> = repository.getAllRumahLocal().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue =  emptyList()
    )

    fun syncDataRumah(token: String){
        viewModelScope.launch {
            repository.pullDataFromServer(token)
        }
    }


    fun tambahRumah(token: String, alamat: String, noRumah: String, rtId: Int, onSuccess: (Int) -> Unit){
        viewModelScope.launch {
            val newId = repository.addNewRumah(token, alamat, noRumah, rtId)
            onSuccess(newId.toInt())
        }
    }

    fun updateRumah(token: String, rumahLocal: RumahEntity, alamatBaru: String, noRumahBaru: String){
        viewModelScope.launch {
            repository.updateRumah(token, rumahLocal, alamatBaru, noRumahBaru)
        }
    }
}