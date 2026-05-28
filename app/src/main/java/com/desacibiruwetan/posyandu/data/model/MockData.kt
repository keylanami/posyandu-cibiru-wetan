package com.desacibiruwetan.posyandu.data.model


data class DummyDetailWarga(
    val name: String,
    val nik: String,
    val gender: String = "Laki-laki",
    val rtRw: String = "RT04 / RW02",
    val noRumah: String = "B-12",
    val noKk: String = "123456789013456",
    val tanggalLahir: String = "12051989",
    val namaPasangan: String = "Istri/Suami Default",
    val pekerjaan: String = "Penjudi handal",
    val noBpjs: String = "1234567890123456",
    val keterangan: String = "Suka bohong pas rapat bareng kepala desa",
    val noKeluarga: String = "004",
    val pendidikanTerakhir: String = "S1 Keperawatan",
    val statusGakin: String = "Non Gakin",

)

object MockData {
    val listWarga = listOf(
        DummyDetailWarga(
            name = "Jaka Sambung",
            nik = "1234567890123456",
            gender = "Laki-laki"
        ),
        DummyDetailWarga(
            name = "Jaka Golok",
            nik = "1234567890123457",
            gender = "Laki-laki"
        ),
        DummyDetailWarga(
            name = "istri atta halilintar (istri)",
            nik = "3204123456780001",
            gender = "Perempuan",
            namaPasangan = "atta halilintar"
        ),
        DummyDetailWarga(
            name = "atta halilintar",
            nik = "3204123456780002",
            gender = "Laki-laki"
        )
    )
}