package com.desacibiruwetan.posyandu.data.repository

import android.util.Log
import com.desacibiruwetan.posyandu.data.local.dao.KiaDao
import com.desacibiruwetan.posyandu.data.local.dao.PeduliStuntingDao
import com.desacibiruwetan.posyandu.data.local.dao.PhbsDao
import com.desacibiruwetan.posyandu.data.local.dao.SiagaKebakaranDao
import com.desacibiruwetan.posyandu.data.local.entity.KiaEntity
import com.desacibiruwetan.posyandu.data.local.entity.PeduliStuntingEntity
import com.desacibiruwetan.posyandu.data.local.entity.PhbsEntity
import com.desacibiruwetan.posyandu.data.local.entity.SiagaKebakaranEntity
import com.desacibiruwetan.posyandu.data.model.KiaRequest
import com.desacibiruwetan.posyandu.data.model.PeduliStuntingRequest
import com.desacibiruwetan.posyandu.data.model.PhbsRequest
import com.desacibiruwetan.posyandu.data.model.SiagaKebakaranRequest
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
        val req = PhbsRequest(
            patuhProtokolKesehatan = patuhProtokolKesehatan,
            rumahJambanSehat = rumahJambanSehat,
            rumahAirBersih = rumahAirBersih,
            kasusDiare = kasusDiare,
            keluargaSadarGizi = keluargaSadarGizi,
            rumahTanpaAsapRokok = rumahTanpaAsapRokok,
            babs = babs
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
        val req = PeduliStuntingRequest(
            bayiLahirPrematur = bayiLahirPrematur,
            bayiBblr = bayiBblr,
            balitaKurangGizi = null,
            balitaStunting = balitaStunting,
            balitaRutinPemeriksaanTumbuhKembang = balitaRutinPemeriksaanTumbuhKembang,
            kehamilanTidakDirencanakan = kehamilanTidakDirencankan,
            jarakKehamilanTerlaluDekat = jarakKehamilanTerlaluDekat
        )

        try {
            if (serverId != null) {
                val res = apiService.putPeduliStunting(token, serverId, req)
                if (res.isSuccessful) stuntingDao.updatePeduliStuntingLocal(update.copy(isSynced = true))
            } else {
                val res = apiService.postPeduliStunting(token, req)
                if (res.isSuccessful && res.body()?.data != null) {
                    stuntingDao.updatePeduliStuntingLocal(
                        update.copy(
                            idPeduliStuntingServer = res.body()!!.data!!.id,
                            isSynced = true
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Offline, Stunting lokal. ${e.message}")
        }
    }


    suspend fun updateDataKia(
        token: String,
        ibuHamilRutinPeriksa: Int?,
        persalinanTenagaKesehatan: Int?,
        kematianIbuNifas: Int?,
        kankerServiks: Int?,
        imunisasiBayiBalita: Int?,
        bayiBalitaSakitTerdata: Int?,
        kematianBayiBalita: Int?
    ) {
        val lokal = kiaDao.getAllKiaDao().firstOrNull()?.firstOrNull()
        val update = lokal?.copy(
            ibuHamilRutinPeriksa = ibuHamilRutinPeriksa,
            persalinanTenagaKesehatan = persalinanTenagaKesehatan,
            kematianIbuNifas = kematianIbuNifas,
            kankerServiks = kankerServiks,
            imunisasiBayiBalita = imunisasiBayiBalita,
            bayiBalitaSakitTerdata = bayiBalitaSakitTerdata,
            kematianBayiBalita = kematianBayiBalita,
            isSynced = false
        ) ?: KiaEntity(
            idKiaLocal = 0,
            idKiaServer = 0,
            ibuHamilRutinPeriksa = ibuHamilRutinPeriksa ?: 0,
            persalinanTenagaKesehatan = persalinanTenagaKesehatan ?: 0,
            kematianIbuNifas = kematianIbuNifas ?: 0,
            kankerServiks = kankerServiks ?: 0,
            imunisasiBayiBalita = imunisasiBayiBalita ?: 0,
            bayiBalitaSakitTerdata = bayiBalitaSakitTerdata ?: 0,
            kematianBayiBalita = kematianBayiBalita ?: 0,
            isSynced = false,
            createdAt = "",
            updatedAt = ""
        )

        if (lokal != null) kiaDao.updateKiaLocal(update)
        else kiaDao.insertKiaLocal(update)

        val serverId = update.idKiaServer
        val req = KiaRequest(
            ibuHamilRutinPeriksa = ibuHamilRutinPeriksa,
            persalinanTenagaKesehatan = persalinanTenagaKesehatan,
            kematianIbuNifas = kematianIbuNifas,
            kankerServiks = kankerServiks,
            imunisasiBayiBalita = imunisasiBayiBalita,
            bayiBalitaSakitTerdata = bayiBalitaSakitTerdata,
            kematianBayiBalita = kematianBayiBalita
        )


        try {
            if (serverId != null) {
                val res = apiService.putKia(token, serverId, req)
                if (res.isSuccessful) kiaDao.updateKiaLocal(update.copy(isSynced = true))
            } else {
                val res = apiService.postKia(token, req)
                if (res.isSuccessful && res.body()?.data != null) {
                    kiaDao.updateKiaLocal(
                        update.copy(
                            idKiaServer = res.body()!!.data!!.id,
                            isSynced = true
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Offline, Kia lokal. ${e.message}")
        }
    }


    suspend fun updateDataSiagaKebakaran(
        token: String,
        kebakaranRumahTangga: Int?,
        kebakaranNonRumahTangga: Int?,
        rumahPunyaAparAtauAir: Int?,
        rumahSemiPermanenKayu: Int?,
        rumahPunyaP3k: Int?,
        kecelakaanRumahTangga: Int?,
        instalasiHydrant: Int?,
    ) {
        val lokal = kebakaranDao.getAllSiagaKebakaranDao().firstOrNull()?.firstOrNull()
        val update = lokal?.copy(
            kebakaranRumahTangga = kebakaranRumahTangga,
            kebakaranNonRumahTangga = kebakaranNonRumahTangga,
            rumahPunyaAparAtauAir = rumahPunyaAparAtauAir,
            rumahSemiPermanenKayu = rumahSemiPermanenKayu,
            rumahPunyaP3k = rumahPunyaP3k,
            kecelakaanRumahTangga = kecelakaanRumahTangga,
            instalasiHydrant = instalasiHydrant,
            isSynced = false
        ) ?: SiagaKebakaranEntity(
            kebakaranRumahTangga = kebakaranRumahTangga,
            kebakaranNonRumahTangga = kebakaranNonRumahTangga,
            rumahPunyaAparAtauAir = rumahPunyaAparAtauAir,
            rumahSemiPermanenKayu = rumahSemiPermanenKayu,
            rumahPunyaP3k = rumahPunyaP3k,
            kecelakaanRumahTangga = kecelakaanRumahTangga,
            instalasiHydrant = instalasiHydrant,
            isSynced = false
        )


        if (lokal != null) kebakaranDao.updateSiagaKebakaran(update)
        else kebakaranDao.insertSiagaKebakaran(update)

        val serverId = update.idSiagaKebakaranServer
        val req = SiagaKebakaranRequest(
            kebakaranRumahTangga = kebakaranRumahTangga,
            kebakaranNonRumahTangga = kebakaranNonRumahTangga,
            rumahPunyaAparAtauAir = rumahPunyaAparAtauAir,
            rumahSemiPermanenKayu = rumahSemiPermanenKayu,
            rumahPunyaP3k = rumahPunyaP3k,
            kecelakaanRumahTangga = kecelakaanRumahTangga,
            instalasiHydrant = instalasiHydrant,
        )

        try {
            if (serverId != null) {
                val res = apiService.putSiagaKebakaran(token, serverId, req)
                if (res.isSuccessful) kebakaranDao.updateSiagaKebakaran(update.copy(isSynced = true))
            } else {
                val res = apiService.postSiagaKebakaran(token, req)
                if (res.isSuccessful && res.body()?.data != null) {
                    kebakaranDao.updateSiagaKebakaran(
                        update.copy(
                            idSiagaKebakaranServer = res.body()!!.data!!.id,
                            isSynced = true
                        )
                    )
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Offline, Kebakaran lokal. ${e.message}")
        }
    }

}
