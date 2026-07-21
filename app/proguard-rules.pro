# Keep model yang dipakai Moshi jika menggunakan reflection
-keep class com.desacibiruwetan.posyandu.data.model.** { *; }

# Keep entity Room
-keep class com.desacibiruwetan.posyandu.data.local.entity.** { *; }

# Simpan informasi stacktrace
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile