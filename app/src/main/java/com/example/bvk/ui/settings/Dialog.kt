package com.example.bvk.ui.settings

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.bvk.R
import com.example.bvk.ui.MainActivity

class SettingsDialogManager(val context : Activity) {
    fun printImportDialog(
    ): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder
            .setMessage(
                R.string.success_import_title
            )
            .setPositiveButton("cancel") { dialog, _ ->
                dialog.cancel()
                context.finishAffinity()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(context, intent, null)
            }
        return dialogBuilder.create()
    }

    fun printExceptionDialog(
        e: Exception,
        incorrectPath: String,
        fullPath: String
    ): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context)
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
}