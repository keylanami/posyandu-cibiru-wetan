# 🏥 Posyandu Cibiru Wetan (GKSTTB)
<p align="center">
  <img src="https://raw.githubusercontent.com/keylanami/posyandu-cibiru-wetan/refs/heads/master/Banner.png" width="200" alt="Banner">
</p>
> **Digitalizing Rural Health Services with Modern Android Excellence.**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.x-blue.svg?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-2024.10-green.svg?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com)
[![Android 16 Ready](https://img.shields.io/badge/Target_SDK-36-orange.svg?style=for-the-badge&logo=android)](https://developer.android.com)
[![Build Status](https://img.shields.io/badge/Build-Optimized_with_R8-red.svg?style=for-the-badge)](https://developer.android.com/studio/build/shrink-code)

**Posyandu Cibiru Wetan** (Gerakan Keluarga Sehat Tanggap Tangguh Bencana) adalah aplikasi manajemen kesehatan desa yang dirancang khusus untuk Kader Posyandu. Aplikasi ini mengubah proses manual berbasis kertas menjadi alur kerja digital yang cepat, aman, dan **Offline-First**.

---

## ✨ Fitur Utama (Core Features)

### 🧩 **Alur Kerja Kontekstual (Contextual Workflow)**
Kami menggunakan pola **"Choose Resident First"**. Kader mencari warga, masuk ke detail, lalu mengelola data kesehatan. Tidak ada lagi pencarian berulang yang membingungkan.

### 🔄 **Sequential Data Sync Engine**
Engine sinkronisasi cerdas yang memastikan integritas relasi data:
1.  **Sync Rumah** 🏠
2.  **Sync Keluarga (KK)** 👨‍👩‍👧‍👦
3.  **Sync Warga (Anggota)** 👤
*Menghindari race condition dan memastikan data tidak hilang saat sinkronisasi pertama kali pada database kosong.*

### 📶 **Offline-First Capabilities**
Bekerja tanpa sinyal? Tidak masalah. Data disimpan di **Room Database** lokal dan akan otomatis diunggah (Sync) saat perangkat mendapatkan koneksi internet.

### 👶 **Modul Kesehatan Spesifik**
*   **Balita**: Pelacakan pertumbuhan (BB/TB) yang presisi.
*   **Bumil**: Manajemen data kehamilan dan ASI Eksklusif.
*   **WUS/PUS & KB**: Pendataan pasangan usia subur dan riwayat kontrasepsi yang terintegrasi.

### 🚀 **Google Play In-App Updates**
Integrasi resmi dengan **Play Core API**. Pengguna mendapatkan notifikasi pembaruan langsung di dalam aplikasi tanpa perlu mengunduh APK manual. Keamanan maksimal tanpa izin `REQUEST_INSTALL_PACKAGES`.

---

## 🛠️ Stack Teknologi (Tech Stack)

*   **UI**: Jetpack Compose (Declarative UI) dengan dukungan penuh **Edge-to-Edge** (Android 15/16 ready).
*   **Arsitektur**: MVVM (Model-View-ViewModel) + Repository Pattern.
*   **Lokal**: Room Persistence Library.
*   **Network**: Retrofit & OkHttp dengan Moshi JSON Converter.
*   **Async**: Kotlin Coroutines & Flow.
*   **Optimization**: R8 Full Mode dengan ProGuard rules yang diperketat untuk Moshi, Room, & Data Schemas.

---

## 🏗️ Struktur Proyek

`com.desacibiruwetan.posyandu/`
├── **data/**
│   ├── **local/**          # DAOs, Entities (Room)
│   ├── **model/**          # DTOs & Request Schemas
│   ├── **network/**        # Retrofit Services & BuildConfig API URLs
│   └── **repository/**     # Data logic & Sequential Sync processing
├── **navigation/**         # SafeNavController & AppNavigation (API 36 Ready)
├── **ui/**
│   ├── **components/**     # Reusable UI (Forms, BottomSheets, Bars)
│   ├── **screen/**         # Feature Screens (Dashboard, Detail, Update)
│   └── **theme/**          # Poppins & Inter Typography, Custom Colors
└── **viewmodel/**          # State management & Business Logic

---

## 🚀 Memulai (Getting Started)

1.  **Clone**: `git clone https://github.com/username/posyandu-cibiru-wetan.git`
2.  **IDE**: Gunakan **Android Studio Ladybug (2024.2.1)** atau yang terbaru.
3.  **Java**: Pastikan JDK diatur ke **Java 17**.
4.  **Sync**: Lakukan Gradle Sync dan jalankan di perangkat (Minimal API 24).

---

## 🔒 Keamanan & Performa

*   **R8 Minification**: Kode dikecilkan dan diobfuskasi untuk mencegah *reverse engineering*.
*   **Resource Shrinking**: Menghapus resource yang tidak digunakan untuk ukuran APK yang sangat kecil.
*   **Edge-to-Edge Support**: Content otomatis menyesuaikan *status bar* dan *navigation bar* menggunakan `statusBarsPadding()`.

---

## 🤝 Kontribusi

Proyek ini dibangun untuk mendukung kesehatan masyarakat Desa Cibiru Wetan. Jika Anda menemukan bug atau ingin menambahkan fitur:
1. Fork proyek ini.
2. Buat branch fitur (`git checkout -b feature/CoolFeature`).
3. Commit perubahan (`git commit -m 'Add CoolFeature'`).
4. Push ke branch (`git push origin feature/CoolFeature`).
5. Buka Pull Request.

---
