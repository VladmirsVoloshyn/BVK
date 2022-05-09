package com.example.bvk.ui

import androidx.lifecycle.*
import com.example.bvk.database.MandrelRepository
import com.example.bvk.model.Mandrel
import com.example.bvk.model.MandrelProcessor
import kotlinx.coroutines.launch

class MandrelViewModel(private val repository: MandrelRepository) : ViewModel() {

    private val mandrelsRoomList: LiveData<List<Mandrel>> = repository.allMandrels.asLiveData()

    private val mandrelsSampleList: MutableLiveData<List<Mandrel>> = MutableLiveData()

    var isSampleCreated = false

    fun createSample(vertexDiameter: Int, heightSoughtFor: Int) {
        val mandrelsList = ArrayList<Mandrel>()
        for (mandrel in mandrelsRoomList.value as ArrayList<Mandrel>) {
            if (mandrel.vertexDiameter.toInt() == vertexDiameter) {
                mandrelsList.add(
                    MandrelProcessor.setDataForMandrel(
                        mandrel,
                        heightSoughtFor
                    )
                )
            }
        }
        mandrelsSampleList.value = mandrelsList
        isSampleCreated = true
        getData()
    }

    fun getData(): LiveData<List<Mandrel>> {
        if (isSampleCreated) {
            return mandrelsSampleList
        }
        return mandrelsRoomList
    }


    //room impl
    fun insert(mandrel: Mandrel) = viewModelScope.launch {
        repository.insert(mandrel)
    }

    fun delete(id: Int) = viewModelScope.launch {
        mandrelsRoomList.value?.get(id)?.let { repository.delete(it.id) }
    }

    fun update(mandrel: Mandrel) = viewModelScope.launch {
        repository.update(mandrel)
    }
}