package com.example.bvk.model.databaseimportexport

import com.example.bvk.model.Mandrel
import com.example.bvk.model.packageschema.PackageSchema

class ExportListManager {
    companion object{
        var exportMandrelsList = ArrayList<Mandrel>()
        var exportSchemasList = ArrayList<PackageSchema>()

        fun setMandrelsExportList(mandrelsList : ArrayList<Mandrel>){
            exportMandrelsList = mandrelsList
        }
        fun setSchemasExportList(schemasList : ArrayList<PackageSchema>){
            exportSchemasList = schemasList
        }
    }
}