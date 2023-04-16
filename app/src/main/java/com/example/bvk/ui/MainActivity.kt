package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.bvk.*
import com.example.bvk.databinding.ActivityMainBinding
import com.example.bvk.ui.settings.SettingsActivity
import com.example.bvk.ui.settings.Theme

class MainActivity : AppCompatActivity(), FragmentCommutator {

    private lateinit var binding: ActivityMainBinding
    private var mandrelFragment = MandrelFragment()

    private var saveIsSampleCreated: Boolean = false
    private var onUpdateModeListener: OnUpdateModeListener? = null

    private var preferences: SharedPreferences? = null
    private lateinit var editor: SharedPreferences.Editor
    private var launchCounter = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        configurePreference()
        checkPermissions()
        registerNewLaunch()
        configureMainFragment()
        setUpTheme()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M &&
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            val writePerm =
                Array(1) { android.Manifest.permission.WRITE_EXTERNAL_STORAGE }
            requestPermissions(writePerm, 1000)
        }
    }

    private fun configureMainFragment() {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(binding.container.id, mandrelFragment).commit()
            onUpdateModeListener = mandrelFragment
    }

    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun registerNewLaunch() {
        launchCounter = preferences!!.getInt(PREFERENCE_KEY_LAUNCH_COUNTER, 0)
        launchCounter += 1
        editor.putInt(PREFERENCE_KEY_LAUNCH_COUNTER, launchCounter)
        editor.apply()
    }

    private fun configurePreference() {
        preferences =
            application.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = preferences!!.edit()
        if (preferences?.getString(THEME_KEY, null) == null) {
            editor.putString(THEME_KEY, DEFAULT_THEME)
        }
        if (launchCounter == 1) {
            Toast.makeText(this, launchCounter.toString(), Toast.LENGTH_SHORT).show()
            editor.putString(PREFERENCE_KEY_PASSWORD, DEFAULT_PASSWORD).apply()
            editor.putInt(PREFERENCE_KEY_ADHESIVE_LINE, 5).apply()
            editor.putInt(PREFERENCE_KEY_MEMBRANE_WEIGHT, 60).apply()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SAMPLE_CREATE_TAG, saveIsSampleCreated)
    }

    override fun getSavingState(isSampleCreated: Boolean) {
        saveIsSampleCreated = isSampleCreated
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.updateModeItem) {
            onUpdateModeListener?.onUpdate()
        } else if (item.itemId == R.id.openSettingsItem) {
            finish()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        resources.getText(R.string.access_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        resources.getText(R.string.access_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        const val DEFAULT_THEME = "light"
    }

    private fun setUpTheme() {
        when (preferences?.getString(THEME_KEY, DEFAULT_THEME)) {
            Theme.LIGHT.desc -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Theme.DARK.desc -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Theme.GREEN.desc -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(R.style.Theme_BVK_Green)
                changeStatusBarColor(this, R.color.greenThemeStatusBarColor)
                supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.card_bg_green))
            }
            Theme.BROWN.desc -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(R.style.Theme_BVK_Brown)
                changeStatusBarColor(this, R.color.brownThemeStatusBarColor)
                supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.card_bg_brown))
            }
        }
    }

    interface OnUpdateModeListener {
        fun onUpdate()
    }
}

