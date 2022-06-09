package com.example.bvk.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bvk.R
import com.example.bvk.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity(),
    ChangePasswordDialogFragment.OnPasswordChangeListener {

    private lateinit var binding: ActivitySettingsBinding

    private var preferences: SharedPreferences? = null
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = application.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = preferences!!.edit()

        setUIMode()

        supportActionBar?.subtitle = getString(R.string.action_bar_settings_label)

        binding.passwordSettingsContainer.setOnClickListener {
            val changePasswordDialogFragment = ChangePasswordDialogFragment(
                preferences!!.getString(
                    PREFERENCE_KEY_PASSWORD,
                    DEFAULT_PASSWORD
                ), this
            )
            changePasswordDialogFragment.show(supportFragmentManager, "PASSWORD")
        }

    }

    companion object {
        const val APP_PREFERENCES = "settings"
        const val PREFERENCE_KEY_PASSWORD = "pass"
        const val PREFERENCE_KEY_LAUNCH_COUNTER = "launch_counter"
        const val PREFERENCE_KEY_ADHESIVE_LINE = "kal"
        const val PREFERENCE_KEY_MEMBRANE_WEIGHT = "kmw"
        const val DEFAULT_PASSWORD = "123"
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUIMode() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.passwordSettingsContainer.background = getDrawable(R.drawable.card_bg)
                binding.adhesiveSaveLineSettingsContainer.background =
                    getDrawable(R.drawable.card_bg)
                binding.membraneDepthSettingsContainer.background = getDrawable(R.drawable.card_bg)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.passwordSettingsContainer.background = getDrawable(R.drawable.card_bg_night)
                binding.adhesiveSaveLineSettingsContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.membraneDepthSettingsContainer.background =
                    getDrawable(R.drawable.card_bg_night)
            }
        }

    }

    override fun onPasswordChanged(password: String) {
        editor.putString(PREFERENCE_KEY_PASSWORD, password).apply()
        Toast.makeText(this, "Password change successful", Toast.LENGTH_SHORT).show()
    }

}