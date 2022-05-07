package com.example.bvk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bvk.database.MandrelRepository
import com.example.bvk.model.Mandrel
import kotlinx.coroutines.launch

class MandrelViewModel(private val repository: MandrelRepository) : ViewModel() {

    val mandrelsList: LiveData<List<Mandrel>> = repository.allMandrels.asLiveData()

    //room impl
    fun insert(mandrel: Mandrel) = viewModelScope.launch {
        repository.insert(mandrel)
    }

    fun delete(id: Int) = viewModelScope.launch {
        mandrelsList.value?.get(id)?.let { repository.delete(it.id) }
    }

    fun update(mandrel: Mandrel) = viewModelScope.launch {
        repository.update(mandrel)
    }
}