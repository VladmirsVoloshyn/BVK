package com.example.bvk

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.bvk.database.mandreldatabase.MandrelRepository
import com.example.bvk.database.mandreldatabase.MandrelRoomDataBase
import com.example.bvk.database.packagedatabase.PackageRepository
import com.example.bvk.database.packagedatabase.PackageRoomDatabase
import com.example.bvk.ui.MainActivity
import com.example.bvk.ui.settings.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Suppress("unused")
class BVKApplication : Application() {

    private var preferences: SharedPreferences? = null
    private lateinit var editor: SharedPreferences.Editor

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        setUpTheme()
    }

    private val mandrelsDataBase by lazy {
        MandrelRoomDataBase.getDataBase(this, applicationScope)
    }

    val mandrelsRepository by lazy {
        MandrelRepository(mandrelsDataBase.mandrelDao())
    }

    private val schemasDataBase by lazy {
        PackageRoomDatabase.getDataBase(this, applicationScope)
    }

    val schemasRepository by lazy {
        PackageRepository(schemasDataBase.packageDao())
    }

    fun finish() {
        this.finish()
    }

    companion object {
        const val DEFAULT_THEME = "light"
        const val THEME_KEY = "theme"
    }

    private fun setUpTheme() {
        when (preferences?.getString(com.example.bvk.THEME_KEY, MainActivity.DEFAULT_THEME)) {
            Theme.LIGHT.desc -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Theme.DARK.desc -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
           // Theme.GREEN.desc -> {
           //     AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
           //     setTheme(R.style.Theme_BVK_Green)
           //     changeStatusBarColor(this, R.color.greenThemeStatusBarColor)
           //     supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.card_bg_green))
           // }
           // Theme.BROWN.desc -> {
           //     AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
           //     setTheme(R.style.Theme_BVK_Brown)
           //     changeStatusBarColor(this, R.color.brownThemeStatusBarColor)
           //     supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.card_bg_brown))
           // }
        }
    }
}