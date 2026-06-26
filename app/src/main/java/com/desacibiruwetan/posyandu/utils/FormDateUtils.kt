package com.desacibiruwetan.posyandu.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private val indonesiaLocale = Locale("id", "ID")

private fun parser(pattern: String, locale: Locale = Locale.US, utc: Boolean = false): SimpleDateFormat =
    SimpleDateFormat(pattern, locale).apply {
        isLenient = false
        if (utc) timeZone = TimeZone.getTimeZone("UTC")
    }

fun normalizeDateForForm(value: String?): String {
    val source = value?.trim().orEmpty()
    if (source.isBlank() || source == "null") return ""

    val rawDigits = source.filter { it.isDigit() }
    if (rawDigits.length == 8 && !source.contains("-")) {
        return "${rawDigits.substring(0, 2)}-${rawDigits.substring(2, 4)}-${rawDigits.substring(4, 8)}"
    }

    val datePart = source.substringBefore("T")
    val patterns = listOf(
        "dd-MM-yyyy" to Locale.US,
        "yyyy-MM-dd" to Locale.US,
        "dd/MM/yyyy" to Locale.US,
        "yyyy/MM/dd" to Locale.US,
        "d MMMM yyyy" to indonesiaLocale,
        "dd MMMM yyyy" to indonesiaLocale
    )
    patterns.forEach { (pattern, locale) ->
        runCatching {
            val parsed = parser(pattern, locale).parse(datePart)
            SimpleDateFormat("dd-MM-yyyy", indonesiaLocale).format(parsed!!)
        }.getOrNull()?.let { return it }
    }

    return ""
}

fun formatDateForDisplay(value: String?): String {
    val normalized = normalizeDateForForm(value)
    if (normalized.isBlank()) return ""
    return runCatching {
        val parsed = parser("dd-MM-yyyy").parse(normalized)
        SimpleDateFormat("d MMMM yyyy", indonesiaLocale).format(parsed!!)
    }.getOrDefault(normalized)
}

fun dateFieldToUtcMillis(value: String?): Long? {
    val normalized = normalizeDateForForm(value)
    if (normalized.isBlank()) return null
    return runCatching {
        parser("dd-MM-yyyy", utc = true).parse(normalized)?.time
    }.getOrNull()
}

fun utcMillisToFormDate(millis: Long): String =
    SimpleDateFormat("dd-MM-yyyy", indonesiaLocale).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(millis)

fun calculateAgeInfo(date: String): Pair<String, String> {
    val normalized = normalizeDateForForm(date)
    if (normalized.isBlank()) return "" to ""

    val birthDate = runCatching {
        parser("dd-MM-yyyy").parse(normalized)
    }.getOrNull() ?: return "" to ""

    val birth = Calendar.getInstance().apply { time = birthDate }
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) age--

    val category = when {
        age < 5 -> "Balita"
        age < 12 -> "Anak-anak"
        age < 18 -> "Remaja"
        age < 60 -> "Dewasa"
        else -> "Lansia"
    }

    return age.toString() to category
}

fun isSameOrAfter(startDate: String?, endDate: String?): Boolean {
    val startMillis = dateFieldToUtcMillis(startDate) ?: return true
    val endMillis = dateFieldToUtcMillis(endDate) ?: return true
    return endMillis >= startMillis
}
