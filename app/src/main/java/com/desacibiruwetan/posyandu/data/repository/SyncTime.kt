package com.desacibiruwetan.posyandu.data.repository

internal fun newestSyncTime(current: String?, candidate: String?): String? {
    if (candidate == null || !Regex("^\\d{4}-\\d{2}-\\d{2}").containsMatchIn(candidate)) return current
    if (current == null) return candidate
    return maxOf(current, candidate)
}
