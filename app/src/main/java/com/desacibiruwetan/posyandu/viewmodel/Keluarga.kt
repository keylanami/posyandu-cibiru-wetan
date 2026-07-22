package com.desacibiruwetan.posyandu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.model.KeluargaOpt
import com.desacibiruwetan.posyandu.data.repository.KeluargaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KeluargaViewmodel(private val repository: KeluargaRepository): ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing = _isSyncing.asStateFlow()

    private val _keluargaOptions = MutableStateFlow<List<com.desacibiruwetan.posyandu.data.model.KeluargaOpt>>(emptyList())
    val keluargaOptions: StateFlow<List<com.desacibiruwetan.posyandu.data.model.KeluargaOpt>> = _keluargaOptions

    val listKeluargaLocal: StateFlow<List<KeluargaEntity>> = repository.getAllKeluargaLocal().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue =  emptyList()
    )


    suspend fun syncDataKeluarga(token: String){
        _isSyncing.value = true
        try {
            repository.pullDataFromServer(token)
        } finally {
            _isSyncing.value = false
        }
    }

    fun tambahKeluarga(
        token: String,
        rumahId: Int,
        rumahServerId: Int?,
        noKk: String,
        statusKepemilikanRumah: String,
        kepemilikanJamban: String?,
        kepemilikanSpal: String?,
        statusEkonomi: String,
        onSuccess: (Int) -> Unit
    ){
        viewModelScope.launch {
            val newId = repository.addNewKeluarga(
                token,
                rumahId,
                rumahServerId,
                noKk,
                statusKepemilikanRumah,
                kepemilikanJamban,
                kepemilikanSpal,
                statusEkonomi
            )
            onSuccess(newId.toInt())
        }
    }

    fun updateKeluarga(
        token: String,
        keluargaLocal: KeluargaEntity,
        noKKBaru: String,
        statusKepemilikanRumahBaru: String,
        kepemilikanJambanBaru: String?,
        kepemilikanSpalBaru: String?,
        statusEkonomiBaru: String
    ){
        viewModelScope.launch {
            repository.updateKeluarga(
                token,
                keluargaLocal,
                noKKBaru,
                statusKepemilikanRumahBaru,
                kepemilikanJambanBaru,
                kepemilikanSpalBaru,
                statusEkonomiBaru
            )
        }
    }


    fun fetchKeluargaOptions(token: String) {
        viewModelScope.launch {
            val localOptions = listKeluargaLocal.value.map {
                KeluargaOpt(
                    id = it.serverId ?: it.localId,
                    noKk = it.noKK,
                    kepalaKeluarga = null
                )
            }
            if (localOptions.isNotEmpty()) {
                _keluargaOptions.value = localOptions
            }

            val serverOptions = repository.getKeluargaOptionsFromServer(token)
            if (serverOptions.isNotEmpty()) {
                val localOnlyOptions = localOptions.filter { local ->
                    serverOptions.none { server -> server.id == local.id }
                }
                _keluargaOptions.value = localOnlyOptions + serverOptions
            }
        }
    }
}
