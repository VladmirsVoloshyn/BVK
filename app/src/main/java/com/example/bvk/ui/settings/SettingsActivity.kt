package com.example.bvk.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bvk.BVKApplication
import com.example.bvk.R
import com.example.bvk.database.mandreldatabase.MandrelRepository
import com.example.bvk.database.packagedatabase.PackageRepository
import com.example.bvk.databinding.ActivitySettingsBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.ui.Dialogs.ConfirmationDialogFragment
import com.example.bvk.ui.MandrelFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity(),
    ChangePasswordDialogFragment.OnPasswordChangeListener,
     ConfirmationDialogFragment.OnConfirmationListener{

    private lateinit var binding: ActivitySettingsBinding

    private var preferences: SharedPreferences? = null
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var mandrelRepository : MandrelRepository
    private lateinit var schemasRepository : PackageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mandrelRepository = (application as BVKApplication).mandrelsRepository
        schemasRepository  = (application as BVKApplication).schemasRepository

        preferences = application.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = preferences!!.edit()

        setUIMode()

        supportActionBar?.subtitle = getString(R.string.action_bar_settings_label)

        binding.restoreDefaultSettingsContainer.setOnClickListener {
            val confirmationDialogFragment = ConfirmationDialogFragment(
                MandrelFragment.RESTORE_DEFAULT_CONFIRMATION_CALL_KEY, listener = this
            )
            confirmationDialogFragment.show(
                supportFragmentManager,
                MandrelFragment.RESTORE_DEFAULT_CONFIRMATION_CALL_KEY
            )
        }

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
        const val PREFERENCE_KEY_ADHESIVE_LINE = "kal"
        const val PREFERENCE_KEY_MEMBRANE_WEIGHT = "kmw"
        const val DEFAULT_PASSWORD = "123"
        const val RESTORE_DEFAULT_CONFIRMATION_CALL_KEY = "RESTORE"
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
                binding.restoreDefaultSettingsContainer.background = getDrawable(R.drawable.card_bg_night)
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

    override fun onDeleteConfirm(position: Int, confirmationKey: String) {
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onRestoreDefaultConfirm(){
        GlobalScope.launch {
            mandrelRepository.deleteAll()
            schemasRepository.deleteAll()

            mandrelRepository.insert(
                Mandrel(
                    mandrelName = "29x1",
                    vertexDiameter = 29.65,
                    baseDiameter = 32.9,
                    height = 75
                )
            )
            mandrelRepository.insert(
                Mandrel(
                    mandrelName = "29x2",
                    vertexDiameter = 29.55,
                    baseDiameter = 33.16,
                    height = 75
                )
            )
            mandrelRepository.insert(
                Mandrel(
                    mandrelName = "29x3",
                    vertexDiameter = 29.88,
                    baseDiameter = 33.81,
                    height = 75
                )
            )
            mandrelRepository.insert(
                Mandrel(
                    mandrelName = "29x4",
                    vertexDiameter = 29.55,
                    baseDiameter = 32.79,
                    height = 75
                )
            )
            mandrelRepository.insert(
                Mandrel(
                    mandrelName = "33x1",
                    vertexDiameter = 33.20,
                    baseDiameter = 36.38,
                    height = 75
                )
            )
            mandrelRepository.insert(
                Mandrel(
                    mandrelName = "43x1",
                    vertexDiameter = 42.65,
                    baseDiameter = 44.56,
                    height = 75
                )
            )
            mandrelRepository.insert(
                Mandrel(
                    mandrelName = "56x1",
                    vertexDiameter = 57.3,
                    baseDiameter = 60.62,
                    height = 75,
                )
            )
            mandrelRepository.insert(
                Mandrel(
                    mandrelName = "61x1",
                    vertexDiameter = 60.6,
                    baseDiameter = 64.4,
                    height = 75,
                )
            )
        }
        finish()
    }

}