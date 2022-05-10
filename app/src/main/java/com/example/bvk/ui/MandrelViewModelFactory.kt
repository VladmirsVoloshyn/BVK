package com.example.bvk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bvk.database.MandrelRepository
import java.lang.IllegalArgumentException

class MandrelViewModelFactory(private val repository: MandrelRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MandrelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MandrelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}