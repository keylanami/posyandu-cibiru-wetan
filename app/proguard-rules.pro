# --- Project Specific Rules ---

# Data Models, Entities, and Schemas (essential for Moshi and Room)
-keep class com.desacibiruwetan.posyandu.data.model.** { *; }
-keep class com.desacibiruwetan.posyandu.data.local.entity.** { *; }
-keep class com.desacibiruwetan.posyandu.data.schema.** { *; }

# --- Retrofit ---
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleAnnotations, RuntimeInvisibleParameterAnnotations
-keep class retrofit2.** { *; }
-keep @retrofit2.http.* interface * { *; }

# --- Moshi ---
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keep class * { @com.squareup.moshi.Json *; }
-keep @com.squareup.moshi.JsonClass class *
-keep class * extends com.squareup.moshi.JsonAdapter
-keep class * { @com.squareup.moshi.Json *; }
-keep @com.squareup.moshi.JsonClass class * { *; }

# --- Room ---
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *

# --- Coroutines ---
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.coroutines.android.HandlerContext {
    val handler;
}

# --- Generic Android & Optimization ---
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn com.squareup.moshi.**

# Preserve the line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
