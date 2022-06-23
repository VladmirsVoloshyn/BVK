package com.example.bvk.model.databaseimportexport.import

import android.os.Environment
import java.io.File

class ImportDataBaseReader(inputUti : String) {

    private var file : File = File(Environment.getExternalStorageDirectory().absolutePath, inputUti)

    fun read(): String {
        return file.bufferedReader().use { it.readText() }
    }
}