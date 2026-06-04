package com.desacibiruwetan.posyandu.utils

import android.util.Log
import com.desacibiruwetan.posyandu.data.repository.AnggotaRepository
import com.desacibiruwetan.posyandu.data.repository.KeluargaRepository
import com.desacibiruwetan.posyandu.data.repository.RumahRepository
import kotlinx.coroutines.flow.first

private const val TAG = "SyncLogger"

suspend fun syncAllAndLog(
    token: String,
    rumahRepo: RumahRepository,
    keluargaRepo: KeluargaRepository,
    anggotaRepo: AnggotaRepository,
) {
    Log.d(TAG, "=== SYNC START ===")
    Log.d(TAG, "Token: Bearer ${token.take(8)}...")

    try {
        rumahRepo.pullDataFromServer("Bearer $token")
        val rumah = rumahRepo.getAllRumahLocal().first()
        Log.d(TAG, "Rumah    => ${rumah.size} data")
        rumah.forEach { Log.d(TAG, "  rumah id=${it.localId} serverId=${it.serverId} noRumah=${it.noRumah} synced=${it.isSynced}") }
    } catch (e: Exception) {
        Log.e(TAG, "Rumah GAGAL: ${e.localizedMessage}")
    }

    try {
        keluargaRepo.pullDataFromServer("Bearer $token")
        val keluarga = keluargaRepo.getAllKeluargaLocal().first()
        Log.d(TAG, "Keluarga => ${keluarga.size} data")
        keluarga.forEach { Log.d(TAG, "  keluarga id=${it.localId} serverId=${it.serverId} noKK=${it.noKK} synced=${it.isSynced}") }
    } catch (e: Exception) {
        Log.e(TAG, "Keluarga GAGAL: ${e.localizedMessage}")
    }

    try {
        anggotaRepo.pullDataFromServer("Bearer $token")
        val anggota = anggotaRepo.getAllAnggotaLocal().first()
        Log.d(TAG, "Anggota  => ${anggota.size} data")
        anggota.forEach { Log.d(TAG, "  anggota id=${it.localId} serverId=${it.serverId} nama=${it.nama} nik=${it.nik} synced=${it.isSynced}") }
    } catch (e: Exception) {
        Log.e(TAG, "Anggota GAGAL: ${e.localizedMessage}")
    }

    Log.d(TAG, "=== SYNC DONE ===")
}
