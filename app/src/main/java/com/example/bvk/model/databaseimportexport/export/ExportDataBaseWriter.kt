package com.example.bvk.model.databaseimportexport.export

import android.annotation.SuppressLint
import android.os.Environment
import com.example.bvk.model.Mandrel
import com.example.bvk.model.packageschema.PackageSchema
import com.fasterxml.jackson.module.kotlin.jsonMapper
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExportDataBaseWriter(
    private val mandrelsList: ArrayList<Mandrel>,
    private val schemasList: ArrayList<PackageSchema>
) {

    private val date = Date()
    @SuppressLint("SimpleDateFormat")
    val simpleDateFormat = SimpleDateFormat("ddMMyyyy-HHmm")
    private val mapper = jsonMapper()


    private fun writeJsonDataString(): String {
        return mapper.writeValueAsString(mandrelsList) + JSON_SEPARATOR + mapper.writeValueAsString(
            schemasList
        )
    }

    fun createDataBaseExportFile() {
        val file = File(
            Environment.getExternalStorageDirectory().absoluteFile,
            FILE_NAME_PREFIX + simpleDateFormat.format(date) + FILE_FORMAT
        )
        val fos = FileOutputStream(file)

        fos.write(writeJsonDataString().toByteArray())
        fos.close()
    }

    companion object {
        private const val JSON_SEPARATOR = '~'
        private const val FILE_FORMAT = ".txt"
        private const val FILE_NAME_PREFIX = "BVKExportData"
    }
}