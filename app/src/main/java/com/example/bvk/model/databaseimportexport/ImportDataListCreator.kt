package com.example.bvk.model.databaseimportexport

import com.example.bvk.model.Mandrel
import com.example.bvk.model.packageschema.PackageSchema
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class ImportDataListCreator(jsonDataString: String) {
    private val mandrelsDataString = jsonDataString.substringBefore(JSON_SEPARATOR)
    private val schemasDataString = jsonDataString.substringAfter(JSON_SEPARATOR)
    private val mapper = jacksonObjectMapper()

    fun getMandrelsImportList(): ArrayList<Mandrel> {
        return mapper.readValue(mandrelsDataString)
    }

    fun getSchemasList(): ArrayList<PackageSchema>{
        return mapper.readValue(schemasDataString)
    }

    companion object {
        private const val JSON_SEPARATOR = "~"
    }
}