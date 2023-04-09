package com.example.bvk.ui.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.Settings
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.example.bvk.BVKApplication
import com.example.bvk.R
import com.example.bvk.database.mandreldatabase.MandrelRepository
import com.example.bvk.database.packagedatabase.PackageRepository
import com.example.bvk.databinding.ActivitySettingsBinding
import com.example.bvk.model.databaseimportexport.ExportListManager
import com.example.bvk.model.databaseimportexport.ImportDataListCreator
import com.example.bvk.model.databaseimportexport.export.ExportDataBaseWriter
import com.example.bvk.model.databaseimportexport.import.ImportDataBaseReader
import com.example.bvk.ui.MainActivity
import com.example.bvk.ui.dialogs.ConfirmationDialogFragment
import com.example.bvk.ui.MandrelFragment
import com.example.bvk.ui.dialogs.EnterPasswordDialogFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

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

    private lateinit var exportDataBaseWriter: ExportDataBaseWriter

    @RequiresApi(Build.VERSION_CODES.O)
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
            intent.putExtra(
                DocumentsContract.EXTRA_INITIAL_URI,
                Environment.getExternalStorageDirectory()
            )
            startActivityForResult(intent, 123)
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
        onBackPressedDispatcher.addCallback(getBackPressedCallBack())
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

    private fun prepareImportData(inputPath: String) {
        val importDataBaseReader = ImportDataBaseReader(inputPath)
        importListCreator = ImportDataListCreator(importDataBaseReader.read())
        importData()

    }

    private fun importData() {
        val listMandrels = mandrelRepository.getAllMandrels.asLiveData().value
        val listSchemas = schemasRepository.getAllSchemas.asLiveData().value

        if (importListCreator.getMandrelsImportList()
                .isEmpty() || importListCreator.getSchemasList().isEmpty()
        ) {
            printWrongFileDialog()
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


    private fun isContains(inputElement: Any, existList: List<Any>): Boolean {
        for (elements in existList) {
            if (elements.hashCode() == inputElement.hashCode()) {
                return true
            }
        }
        return false
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if (data != null) {
                val uri = data.data
                val path = uri?.path?.substringAfter(':')
                try {
                    prepareImportData(path ?: "wrong")
                    printImportDialog(path.toString(), data.data?.path.toString()).show()
                } catch (e: Exception) {
                    printExceptionDialog(e, path.toString(), data.data?.path.toString()).show()
                }
            }
        }
    }

    private fun printWrongFileDialog(

    ): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setMessage(
                "IncorrectFile"
            )
            .setPositiveButton("cancel") { dialog, _ ->
                dialog.cancel()
                finishAffinity()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        return dialogBuilder.create()
    }

    private fun printImportDialog(
        correctPath: String,
        fullPath: String
    ): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setMessage(
                "SUCCESS\n" +
                        "PATH : $correctPath\n" +
                        "FULL PATH : $fullPath"
            )
            .setPositiveButton("cancel") { dialog, _ ->
                dialog.cancel()
                finishAffinity()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        return dialogBuilder.create()
    }

    private fun printExceptionDialog(
        e: Exception,
        incorrectPath: String,
        fullPath: String
    ): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setMessage(
                "ERROR : ${e.message}\n" +
                        "INCORRECT PATH : $incorrectPath\n" +
                        "FULL PATH : $fullPath"
            )
            .setPositiveButton("cancel") { dialog, _ ->
                dialog.cancel()
            }
        return dialogBuilder.create()
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