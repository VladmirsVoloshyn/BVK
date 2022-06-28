package com.example.bvk.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bvk.BVKApplication
import com.example.bvk.R
import com.example.bvk.database.mandreldatabase.MandrelRepository
import com.example.bvk.database.packagedatabase.PackageRepository
import com.example.bvk.databinding.ActivitySettingsBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.model.databaseimportexport.ExportListManager
import com.example.bvk.model.databaseimportexport.ImportDataListCreator
import com.example.bvk.model.databaseimportexport.export.ExportDataBaseWriter
import com.example.bvk.model.databaseimportexport.import.ImportDataBaseReader
import com.example.bvk.ui.dialogs.ConfirmationDialogFragment
import com.example.bvk.ui.MandrelFragment
import com.example.bvk.ui.dialogs.EnterPasswordDialogFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity(),
    ChangePasswordDialogFragment.OnPasswordChangeListener,
    ConfirmationDialogFragment.OnConfirmationListener, SetValueDialogFragment.OnValueSetListener,
    EnterPasswordDialogFragment.OnPasswordEnterListener {

    private lateinit var binding: ActivitySettingsBinding

    private var preferences: SharedPreferences? = null
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var importListCreator: ImportDataListCreator

    private lateinit var mandrelRepository: MandrelRepository
    private lateinit var schemasRepository: PackageRepository

    private lateinit var exportDataBaseWriter : ExportDataBaseWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mandrelRepository = (application as BVKApplication).mandrelsRepository
        schemasRepository = (application as BVKApplication).schemasRepository

        exportDataBaseWriter = ExportDataBaseWriter(
            ExportListManager.exportMandrelsList,
            ExportListManager.exportSchemasList
        )

        preferences = application.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = preferences!!.edit()

        setUIMode()

        supportActionBar?.subtitle = getString(R.string.action_bar_settings_label)

        binding.adhesiveSaveLineSettingsContainer.setOnClickListener {
            val setValueDialogFragment = SetValueDialogFragment(
                CALL_KEY_ADHESIVE,
                preferences!!.getInt(
                    PREFERENCE_KEY_ADHESIVE_LINE, 5
                ), this
            )
            setValueDialogFragment.show(supportFragmentManager, "NEW_VALUE")
        }

        binding.membraneDepthSettingsContainer.setOnClickListener {
            val setValueDialogFragment = SetValueDialogFragment(
                CALL_KEY_MEMBRANE_DEPTH,
                preferences!!.getInt(
                    PREFERENCE_KEY_MEMBRANE_WEIGHT, 60
                ), this
            )
            setValueDialogFragment.show(supportFragmentManager, "NEW_VALUE")
        }

        binding.exportContainer.setOnClickListener {
            val enterPasswordDialogFragment = EnterPasswordDialogFragment(
                preferences!!.getString(
                    PREFERENCE_KEY_PASSWORD, "123"
                ), this
            )
            enterPasswordDialogFragment.show(supportFragmentManager, "SET_PASSWORD")
        }
        binding.importContainer.setOnClickListener {
            requestStorageAccessPermission()
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/*"
            startActivityForResult(Intent.createChooser(intent, "select txt"), 123)
        }

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

    private fun prepareImportData(inputPath: String) {
        val importDataBaseReader = ImportDataBaseReader(inputPath)
        println(importDataBaseReader.read())
        importListCreator = ImportDataListCreator(importDataBaseReader.read())
        importData()
        finishAffinity()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun importData() {
        GlobalScope.launch {
            mandrelRepository.deleteAll()
            schemasRepository.deleteAll()

            for (mandrel in importListCreator.getMandrelsImportList()) {
                mandrelRepository.insert(mandrel)
            }
            for (schema in importListCreator.getSchemasList()) {
                schemasRepository.insert(schema)
            }

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if (data != null) {
                val uri = data.data
                val path = uri?.path?.substringAfter(':')
                println(path)
                prepareImportData(path!!)
                Toast.makeText(this, "File is chosen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val APP_PREFERENCES = "settings"
        const val PREFERENCE_KEY_PASSWORD = "pass"
        const val PREFERENCE_KEY_ADHESIVE_LINE = "kal"
        const val PREFERENCE_KEY_MEMBRANE_WEIGHT = "kmw"
        const val DEFAULT_PASSWORD = "123"
        const val RESTORE_DEFAULT_CONFIRMATION_CALL_KEY = "RESTORE"
        private const val CALL_KEY_ADHESIVE = "adhesive"
        private const val CALL_KEY_MEMBRANE_DEPTH = "depth"
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
                binding.restoreDefaultSettingsContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.passwordSettingsContainer.background = getDrawable(R.drawable.card_bg_night)
                binding.adhesiveSaveLineSettingsContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.membraneDepthSettingsContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.importContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.exportContainer.background =
                    getDrawable(R.drawable.card_bg_night)
            }
        }

    }

    private fun requestStorageAccessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse("package:${applicationContext.packageName}")
                ContextCompat.startActivity(this, intent, null)
            }
        }
    }

    override fun onPasswordChanged(password: String) {
        editor.putString(PREFERENCE_KEY_PASSWORD, password).apply()
        Toast.makeText(this, "Password change successful", Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteConfirm(position: Int, confirmationKey: String) {
    }

    override fun onRestoreDefaultConfirm() {
        println("do nothing")
    }

    override fun onValueSet(newValue: Int, callKey: String) {
        if (callKey == CALL_KEY_ADHESIVE) {
            editor.putInt(PREFERENCE_KEY_ADHESIVE_LINE, newValue).apply()
        }
        if (callKey == CALL_KEY_MEMBRANE_DEPTH) {
            editor.putInt(PREFERENCE_KEY_MEMBRANE_WEIGHT, newValue).apply()
        }
    }

    override fun onPasswordEnter() {
        requestStorageAccessPermission()
        exportDataBaseWriter.createDataBaseExportFile()
        Toast.makeText(this, "export file created", Toast.LENGTH_SHORT).show()
    }

}