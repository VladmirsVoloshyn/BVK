package com.example.bvk.ui

import androidx.lifecycle.*
import com.example.bvk.database.MandrelRepository
import com.example.bvk.model.Mandrel
import com.example.bvk.model.sample.SampleCreator
import kotlinx.coroutines.launch

class MandrelViewModel(private val repository: MandrelRepository) : ViewModel() {

    private val mandrelsRoomList: LiveData<List<Mandrel>> = repository.allMandrels.asLiveData()

    private val mandrelsSampleList: MutableLiveData<List<Mandrel>> = MutableLiveData()

    var isSampleCreated = false

    fun createSample(vertexDiameter: Int, heightSoughtFor: Int) {
        mandrelsSampleList.value = SampleCreator.crate(mandrelsRoomList.value as ArrayList<Mandrel>, vertexDiameter, heightSoughtFor)
        isSampleCreated = true
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