package com.desacibiruwetan.posyandu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.repository.KeluargaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KeluargaViewmodel(private val repository: KeluargaRepository): ViewModel() {

    private val _keluargaOptions = kotlinx.coroutines.flow.MutableStateFlow<List<com.desacibiruwetan.posyandu.data.model.KeluargaOpt>>(emptyList())
    val keluargaOptions: StateFlow<List<com.desacibiruwetan.posyandu.data.model.KeluargaOpt>> = _keluargaOptions

    val listKeluargaLocal: StateFlow<List<KeluargaEntity>> = repository.getAllKeluargaLocal().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue =  emptyList()
    )


    fun syncDataKeluarga(token: String){
        viewModelScope.launch {
            repository.pullDataFromServer(token)
        }
    }

    fun tambahKeluarga(
        token: String,
        rumahId: Int,
        noKk: String,
        isNgontrak: Boolean,
        isGakin: Boolean,
        noRumahForApi: Int = rumahId,
        onSuccess: (Int) -> Unit
    ){
        viewModelScope.launch {
            val newId = repository.addNewKeluarga(token, rumahId, noKk, isNgontrak, isGakin, noRumahForApi)
            onSuccess(newId.toInt())
        }
    }

    fun updateKeluarga(token: String, keluargaLocal: KeluargaEntity, noKKBaru: String, isNgontrakBaru: Boolean, isGakinBaru: Boolean){
        viewModelScope.launch {
            repository.updateKeluarga(token, keluargaLocal, noKKBaru, isNgontrakBaru, isGakinBaru)
        }
    }


    fun fetchKeluargaOptions(token: String) {
        viewModelScope.launch {
            _keluargaOptions.value = repository.getKeluargaOptionsFromServer(token)
        }
    }
}
