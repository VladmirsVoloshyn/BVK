package com.example.bvk.ui

import androidx.lifecycle.*
import com.example.bvk.database.mandreldatabase.MandrelRepository
import com.example.bvk.database.packagedatabase.PackageRepository
import com.example.bvk.model.Mandrel
import com.example.bvk.model.databaseimportexport.ExportListManager
import com.example.bvk.model.packageschema.PackageSchema
import com.example.bvk.model.sample.SampleCapParameters
import com.example.bvk.model.sample.SampleCreator
import kotlinx.coroutines.launch

class MandrelViewModel(
    private val mandrelRepository: MandrelRepository,
    private val schemasRepository: PackageRepository
) : ViewModel() {

    //schema values
    private val schemasRoomList: LiveData<List<PackageSchema>> =
        schemasRepository.allSchemas.asLiveData()

    //mandrel values
    private val mandrelsRoomList: LiveData<List<Mandrel>> =
        mandrelRepository.allMandrels.asLiveData()
    var mandrelsSampleList: MutableLiveData<List<Mandrel>> = MutableLiveData()
    var isSampleCreated = false
    var isAdministratorMode = false
    var isMandrelViewMode = true
    var sampleCapParameters = SampleCapParameters()

    //schemas impl
    fun getSchemasData(): LiveData<List<PackageSchema>> {
        return schemasRoomList
    }

    fun findPackageSchema(sampleCapParameters: SampleCapParameters): PackageSchema? {
        for (schemas in schemasRoomList.value as ArrayList<PackageSchema>) {
            if (schemas.capVertexDiameter == sampleCapParameters.capVertexDiameter && schemas.capHeight == sampleCapParameters.capHeight)
                return schemas
        }
        return null
    }

    fun findPackageSchemaList(sampleCapParameters: SampleCapParameters): List<PackageSchema>? {
        val listSchemas = ArrayList<PackageSchema>()
        for (schemas in schemasRoomList.value as ArrayList<PackageSchema>) {
            if (schemas.capVertexDiameter == sampleCapParameters.capVertexDiameter && schemas.capHeight == sampleCapParameters.capHeight)
                listSchemas.add(schemas)
        }
        if(listSchemas.isEmpty()){
            for (schemas in schemasRoomList.value as ArrayList<PackageSchema>) {
                if (schemas.capVertexDiameter == sampleCapParameters.capVertexDiameter)
                    listSchemas.add(schemas)
            }

        }
        return listSchemas
    }

    //mandrel impl
    fun createSample(inputSampleCapParameters: SampleCapParameters) = viewModelScope.launch {
        sampleCapParameters = inputSampleCapParameters
        mandrelsSampleList.value = SampleCreator.crate(
            mandrelsRoomList.value as ArrayList<Mandrel>,
            inputSampleCapParameters
        )
        isSampleCreated = true
    }

    fun getItem(position: Int): Mandrel? = mandrelsRoomList.value?.get(position)

    fun getMandrelsData(): LiveData<List<Mandrel>> {
        if (isSampleCreated) {
            return mandrelsSampleList
        }
        return mandrelsRoomList
    }

    fun getMandrelUniqueNames(): ArrayList<String> {
        val mandrelsUniqueNamesList = ArrayList<String>()
        for (mandrel in mandrelsRoomList.value as ArrayList) {
            mandrelsUniqueNamesList.add(mandrel.mandrelName)
        }
        return mandrelsUniqueNamesList
    }

    fun getSchemasUniqueNames(): ArrayList<String> {
        val schemasUniqueNamesList = ArrayList<String>()
        for (schema in schemasRoomList.value as ArrayList) {
            schemasUniqueNamesList.add(schema.schemaName)
        }
        return schemasUniqueNamesList
    }

    //room schemas impl
    fun insertSchema(schema: PackageSchema) = viewModelScope.launch {
        schemasRepository.insert(schema)
    }

    fun deleteSchema(id: Int) = viewModelScope.launch {
        schemasRoomList.value?.get(id)?.let { schemasRepository.delete(it.id) }
    }

    fun deleteAllSchemas() = viewModelScope.launch {
        schemasRepository.deleteAll()
    }

    fun updateSchema(schema: PackageSchema) = viewModelScope.launch {
        schemasRepository.update(schema)
    }

    //room mandrel impl
    fun insertMandrel(mandrel: Mandrel) = viewModelScope.launch {
        mandrelRepository.insert(mandrel)
    }

    fun deleteMandrel(id: Int) = viewModelScope.launch {
        mandrelsRoomList.value?.get(id)?.let { mandrelRepository.delete(it.id) }
    }

    fun deleteAllMandrels() = viewModelScope.launch {
        mandrelRepository.deleteAll()
    }

    fun updateMandrel(mandrel: Mandrel) = viewModelScope.launch {
        mandrelRepository.update(mandrel)
    }
}