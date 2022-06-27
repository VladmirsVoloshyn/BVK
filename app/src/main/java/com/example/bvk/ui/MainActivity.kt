package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.bvk.R
import com.example.bvk.databinding.ActivityMainBinding
import com.example.bvk.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), FragmentCommutator {

    private lateinit var binding: ActivityMainBinding
    private var mandrelFragment = MandrelFragment()
    private var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    private var saveIsSampleCreated: Boolean = false
    private var onUpdateModeListener: MandrelFragment? = null

    private var preferences: SharedPreferences? = null
    private lateinit var editor: SharedPreferences.Editor
    private var launchCounter = 0


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M &&
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            val writePerm =
                Array(1) { android.Manifest.permission.WRITE_EXTERNAL_STORAGE }
            requestPermissions(writePerm, 1000)
        }

        preferences =
            application.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = preferences!!.edit()
        registerNewLaunch()

        setDefaultPreferences()

        if (savedInstanceState == null) {
            fragmentTransaction.replace(binding.container.id, mandrelFragment).commit()
        }
        onUpdateModeListener = mandrelFragment
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


    }

    private fun registerNewLaunch() {
        launchCounter = preferences!!.getInt(PREFERENCE_KEY_LAUNCH_COUNTER, 0)
        launchCounter += 1
        editor.putInt(PREFERENCE_KEY_LAUNCH_COUNTER, launchCounter)
        editor.apply()
    }

    private fun setDefaultPreferences() {
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
            onUpdateModeListener?.onUpdateModeClicked()
        } else if (item.itemId == R.id.openSettingsItem) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    interface IfUpdateButtonClickedListener {
        fun onUpdateModeClicked()
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
                    Toast.makeText(this, "доступ разрешен", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "доступ запрещен", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val IS_SAMPLE_CREATE_TAG = "is_sample_create"
        const val APP_PREFERENCES = "settings"
        const val PREFERENCE_KEY_PASSWORD = "pass"
        const val PREFERENCE_KEY_LAUNCH_COUNTER = "launch_counter"
        const val PREFERENCE_KEY_ADHESIVE_LINE = "kal"
        const val PREFERENCE_KEY_MEMBRANE_WEIGHT = "kmw"
        const val DEFAULT_PASSWORD = "123"
    }
}