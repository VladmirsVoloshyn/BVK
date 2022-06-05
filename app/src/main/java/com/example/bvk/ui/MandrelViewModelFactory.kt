package com.example.bvk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bvk.database.mandreldatabase.MandrelRepository
import com.example.bvk.database.packagedatabase.PackageRepository
import java.lang.IllegalArgumentException

class MandrelViewModelFactory(private val mandrelRepository: MandrelRepository, private val schemaRepository: PackageRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MandrelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MandrelViewModel(mandrelRepository, schemaRepository) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}