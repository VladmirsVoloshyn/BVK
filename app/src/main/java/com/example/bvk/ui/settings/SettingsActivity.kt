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
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.example.bvk.*
import com.example.bvk.database.mandreldatabase.MandrelRepository
import com.example.bvk.database.packagedatabase.PackageRepository
import com.example.bvk.databinding.ActivitySettingsBinding
import com.example.bvk.model.databaseimportexport.ExportListManager
import com.example.bvk.model.databaseimportexport.ImportDataListCreator
import com.example.bvk.model.databaseimportexport.export.ExportDataBaseWriter
import com.example.bvk.model.databaseimportexport.import.ImportDataBaseReader
import com.example.bvk.ui.MainActivity
import com.example.bvk.ui.dialogs.EnterPasswordDialogFragment
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.regex.Pattern

class SettingsActivity : AppCompatActivity(),
    ChangePasswordDialogFragment.OnPasswordChangeListener,
    SetValueDialogFragment.OnValueSetListener,
    EnterPasswordDialogFragment.OnPasswordEnterListener,
    ChangeThemeDialogFragment.OnThemeChangeListener {

    private lateinit var binding: ActivitySettingsBinding

    private var preferences: SharedPreferences? = null
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var importListCreator: ImportDataListCreator

    private lateinit var mandrelRepository: MandrelRepository
    private lateinit var schemasRepository: PackageRepository

    private lateinit var exportDataBaseWriter: ExportDataBaseWriter

    private val dialogManager = SettingsDialogManager(this)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareSystem()
        setUIMode()
        if (preferences?.getString(THEME_KEY, null) != Theme.DARK.desc) {
            setUpTheme()
        }

        binding.themeContainer.setOnClickListener {
            val changeThemeDialog =
                ChangeThemeDialogFragment(this, preferences?.getString(THEME_KEY, null) ?: "light")
            changeThemeDialog.show(supportFragmentManager, "NEW_THEME")
        }

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
            requestStorageAccessPermission()
            val enterPasswordDialogFragment = EnterPasswordDialogFragment(
                preferences!!.getString(
                    PREFERENCE_KEY_PASSWORD, "123"
                ), this
            )
            enterPasswordDialogFragment.show(supportFragmentManager, "SET_PASSWORD")
        }
        binding.importContainer.setOnClickListener {
            requestStorageAccessPermission()
            prepareFilePicker()
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


    private fun getBackPressedCallBack(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun prepareFilePicker() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        MaterialFilePicker()
            .withActivity(this)
            .withRequestCode(REQUEST_CODE)
            .withFilter(Pattern.compile(".*\\.$EXPORT_FILE_FORMAT$"))
            .withFilterDirectories(false)
            .withHiddenFiles(true)
            .withTitle("Choose file")
            .withActivity(this)
            .start()
    }

    private fun importData(inputPath: String) {
        val importDataBaseReader = ImportDataBaseReader(inputPath)
        importListCreator = ImportDataListCreator(importDataBaseReader.read())
        val listMandrels = mandrelRepository.getAllMandrels.asLiveData().value
        val listSchemas = schemasRepository.getAllSchemas.asLiveData().value

        if (importListCreator.getMandrelsImportList().isEmpty()) {
            Toast.makeText(this, "wrong file", Toast.LENGTH_SHORT).show()
        } else {
            CoroutineScope(IO).launch {
                for (mandel in importListCreator.getMandrelsImportList()) {
                    if (!isContains(mandel, listMandrels.orEmpty())) {
                        mandrelRepository.insert(mandel)
                    }
                }
                for (schema in importListCreator.getSchemasList()) {
                    if (!isContains(schema, listSchemas.orEmpty())) {
                        schemasRepository.insert(schema)
                    }
                }
            }
        }
    }

    private fun setUpTheme() {
        when (preferences?.getString(THEME_KEY, MainActivity.DEFAULT_THEME)) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        setUpTheme()
        val theme = AppCompatDelegate.getDefaultNightMode()
        AppCompatDelegate.setDefaultNightMode(theme)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            try {
                importData(filePath ?: "wrong")
                dialogManager.printImportDialog().show()
            } catch (e: Exception) {
                dialogManager.printExceptionDialog(
                    e,
                    filePath.toString(),
                    data?.data?.path.toString()
                ).show()
            }
        }

    }

    companion object {

        private const val REQUEST_CODE = 989
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUIMode() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.passwordSettingsContainer.background = getDrawable(R.drawable.card_bg)
                binding.adhesiveSaveLineSettingsContainer.background =
                    getDrawable(R.drawable.card_bg)
                binding.membraneDepthSettingsContainer.background =
                    getDrawable(R.drawable.card_bg)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.restoreDefaultSettingsContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.passwordSettingsContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.adhesiveSaveLineSettingsContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.membraneDepthSettingsContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.importContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.exportContainer.background =
                    getDrawable(R.drawable.card_bg_night)
                binding.themeContainer.background =
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

    private fun prepareSystem() {
        supportActionBar?.subtitle = getString(R.string.action_bar_settings_label)
        mandrelRepository = (application as BVKApplication).mandrelsRepository
        schemasRepository = (application as BVKApplication).schemasRepository

        exportDataBaseWriter = ExportDataBaseWriter(
            ExportListManager.exportMandrelsList,
            ExportListManager.exportSchemasList
        )

        onBackPressedDispatcher.addCallback(getBackPressedCallBack())
        preferences = application.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = preferences!!.edit()
    }

    override fun onThemeChanged(theme: String) {
        editor.putString(THEME_KEY, theme).apply()
        finishAffinity()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}