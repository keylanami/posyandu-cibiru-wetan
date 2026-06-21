package com.desacibiruwetan.posyandu.data.repository

import android.util.Log
import com.desacibiruwetan.posyandu.data.local.dao.KiaDao
import com.desacibiruwetan.posyandu.data.local.dao.PeduliStuntingDao
import com.desacibiruwetan.posyandu.data.local.dao.PhbsDao
import com.desacibiruwetan.posyandu.data.local.dao.SiagaKebakaranDao
import com.desacibiruwetan.posyandu.data.local.entity.PeduliStuntingEntity
import com.desacibiruwetan.posyandu.data.local.entity.PhbsEntity
import com.desacibiruwetan.posyandu.data.model.PeduliStuntingReq
import com.desacibiruwetan.posyandu.data.model.PhbsReq
import com.desacibiruwetan.posyandu.data.network.ApiService
import kotlinx.coroutines.flow.firstOrNull


private const val TAG = "PilotRepo"

class PilotRepository(
    private val apiService: ApiService,
    private val phbsDao: PhbsDao,
    private val stuntingDao: PeduliStuntingDao,
    private val kiaDao: KiaDao,
    private val kebakaranDao: SiagaKebakaranDao
) {

    suspend fun updateDataPhbs(
        token: String, patuhProtokolKesehatan: Int?, rumahJambanSehat: Int?,
        rumahAirBersih: Int?, kasusDiare: Int?, keluargaSadarGizi: Int?,
        rumahTanpaAsapRokok: Int?, babs: Int?
    ) {
        val phbsLokal = phbsDao.getAllPhbsDao().firstOrNull()?.firstOrNull()

        val phbsUpdate = phbsLokal?.copy(
            patuhProtokolKesehatan = patuhProtokolKesehatan, rumahJambanSehat = rumahJambanSehat,
            rumahAirBersih = rumahAirBersih, kasusDiare = kasusDiare,
            keluargaSadarGizi = keluargaSadarGizi, rumahTanpaAsapRokok = rumahTanpaAsapRokok,
            babs = babs, isSynced = false
        ) ?: PhbsEntity(
            patuhProtokolKesehatan = patuhProtokolKesehatan, rumahJambanSehat = rumahJambanSehat,
            rumahAirBersih = rumahAirBersih, kasusDiare = kasusDiare,
            keluargaSadarGizi = keluargaSadarGizi, rumahTanpaAsapRokok = rumahTanpaAsapRokok,
            babs = babs, isSynced = false
        )

        if (phbsLokal != null) phbsDao.updatePhbsLocal(phbsUpdate)
        else phbsDao.insertPhbsLocal(phbsUpdate)

        val serverId = phbsUpdate.idPhbsServer
        val req = PhbsReq(
            patuhProtokolKesehatan,
            rumahJambanSehat,
            rumahAirBersih,
            kasusDiare,
            keluargaSadarGizi,
            rumahTanpaAsapRokok,
            babs
        )

        try {
            if (serverId != null) {
                val res = apiService.putPhbs(token, serverId, req)
                if (res.isSuccessful) phbsDao.updatePhbsLocal(phbsUpdate.copy(isSynced = true))
            } else {
                val res = apiService.postPhbs(token, req)
                if (res.isSuccessful && res.body()?.data != null) {
                    phbsDao.updatePhbsLocal(
                        phbsUpdate.copy(
                            idPhbsServer = res.body()!!.data!!.id,
                            isSynced = true
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Offline, PHBS lokal. ${e.message}")
        }
    }

    suspend fun updateDataPeduliStunting(
        token: String,
        bayiLahirPrematur: Int?,
        bayiBblr: Int?,
        balitaStunting: Int?,
        balitaRutinPemeriksaanTumbuhKembang: Int?,
        kehamilanTidakDirencankan: Int?,
        jarakKehamilanTerlaluDekat: Int?
    ) {
        val lokal = stuntingDao.getAllPeduliStuntingDao().firstOrNull()?.firstOrNull()

        val update = lokal?.copy(
            bayiLahirPrematur = bayiLahirPrematur,
            bayiBblr = bayiBblr,
            balitaStunting = balitaStunting,
            balitaRutinPemeriksaanTumbuhKembang = balitaRutinPemeriksaanTumbuhKembang,
            kehamilanTidakDirencankan = kehamilanTidakDirencankan,
            jarakKehamilanTerlaluDekat = jarakKehamilanTerlaluDekat,
            isSynced = false
        ) ?: PeduliStuntingEntity(
            bayiLahirPrematur = bayiLahirPrematur,
            bayiBblr = bayiBblr,
            balitaStunting = balitaStunting,
            balitaRutinPemeriksaanTumbuhKembang = balitaRutinPemeriksaanTumbuhKembang,
            kehamilanTidakDirencankan = kehamilanTidakDirencankan,
            jarakKehamilanTerlaluDekat = jarakKehamilanTerlaluDekat,
            isSynced = false
        )

        if (lokal != null) stuntingDao.updatePeduliStuntingLocal(update)
        else stuntingDao.insertPeduliStuntingLocal(update)

        val serverId = update.idPeduliStuntingServer
        val req = PeduliStuntingReq(
            bayiLahirPrematur,
            bayiBblr,
            balitaStunting,
            balitaRutinPemeriksaanTumbuhKembang,
            kehamilanTidakDirencankan,
            jarakKehamilanTerlaluDekat
        )

        try {
            if (serverId != null) apiService.putPeduliStunting(token, serverId, req) else apiService.postPeduliStunting(token, req)
        } catch (e: Exception) {
            Log.e(TAG, "Offline, Stunting lokal. ${e.message}")
        }
    }

}