package com.desacibiruwetan.posyandu

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation tests for the core functionality of the Posyandu Cibiru Wetan app.
 * Redone to match the exact implementation of components and labels in the project.
 */
@RunWith(AndroidJUnit4::class)
class AppFunctionalityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Test Case 1.a: Autentikasi (Login)
     * Matches LoginScreen.kt labels and Dashboard header greeting.
     */
    @Test
    fun loginFlowTest() {
        // Use test tags for robust selection
        composeTestRule.onNodeWithTag("emailField").performTextInput("kaderrr@gmail.com")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("password")
        
        // PrimaryButton with test tag
        composeTestRule.onNodeWithTag("loginButton").performClick()
        
        // Dashboard header uses "Selamat bertugas, $userName"
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("Selamat bertugas", substring = true).fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Selamat bertugas", substring = true).assertIsDisplayed()
    }

    /**
     * Test Case 1.b: Input Data Warga
     * Matches CariWargaScreen.kt and AppSearchDropdownField.kt behavior.
     */
    @Test
    fun tambahWargaTest() {
        // AppNavBar uses "Data" for the residents section
        composeTestRule.onNodeWithText("Data").performClick()
        
        // CariWargaScreen uses PrimaryFab with label "Tambah warga"
        composeTestRule.onNodeWithText("Tambah warga").performClick()

        // 1. Test Searchable Dropdown
        composeTestRule.onNodeWithText("Pilih Keluarga").performClick()
        // Type into the editable dropdown
        composeTestRule.onNodeWithText("Cari Pilih Keluarga...", substring = true).performTextInput("3201")

        // Select filtered result
        composeTestRule.onNode(hasText("KK 3201", substring = true)).performClick()

        // 2. Fill Identity Data
        composeTestRule.onNodeWithText("Nama Lengkap").performTextInput("Andi Pratama")
        composeTestRule.onNodeWithText("NIK").performTextInput("3201010101010001")
        composeTestRule.onNodeWithText("Tempat Lahir").performTextInput("Bandung")
        
        // 3. Save
        composeTestRule.onNodeWithText("Simpan Warga Baru").performClick()
        
        // Verify return to list (CariWargaScreen search bar placeholder)
        composeTestRule.onNodeWithText("Cari warga, rumah, KK", substring = true).assertIsDisplayed()
    }

    /**
     * Test Case 1.c: Input Data Rumah
     * Matches RumahKeluargaScreen.kt FAB and form fields.
     */
    @Test
    fun tambahRumahTest() {
        // Navigate via Dashboard "Tambah rumah/KK" button or Navbar "Data" then Switch section
        composeTestRule.onNodeWithText("Data").performClick()
        composeTestRule.onNodeWithText("Rumah").performClick()
        
        // FAB text is "Tambah Rumah"
        composeTestRule.onNodeWithText("Tambah Rumah").performClick()
        
        composeTestRule.onNodeWithText("Alamat Lengkap").performTextInput("Jl. Bakti No. 10")
        composeTestRule.onNodeWithText("Dusun").performClick()
        composeTestRule.onNodeWithText("1").performClick()
        
        composeTestRule.onNodeWithText("Simpan rumah").performClick()
        
        // Verify list entry
        composeTestRule.onNodeWithText("Jl. Bakti No. 10").assertIsDisplayed()
    }

    /**
     * Test Case 1.d: Input Data Keluarga
     * Matches KeluargaFormCard labels in RumahKeluargaScreen.kt.
     */
    @Test
    fun tambahKeluargaTest() {
        composeTestRule.onNodeWithText("Data").performClick()
        composeTestRule.onNodeWithText("Rumah").performClick()
        
        // Find a house card and click "Tambah KK" (CompactAction)
        composeTestRule.onAllNodesWithText("Tambah KK").onFirst().performClick()
        
        composeTestRule.onNodeWithText("No KK").performTextInput("3201234567890001")
        
        // Select Status
        composeTestRule.onNodeWithText("Status tempat tinggal").performClick()
        composeTestRule.onNodeWithText("Milik Sendiri").performClick()
        
        composeTestRule.onNodeWithText("Simpan KK").performClick()
        
        // Verify in list
        composeTestRule.onNodeWithText("KK 3201234567890001").assertIsDisplayed()
    }
}
