package com.example.bvk.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bvk.database.mandreldatabase.MandrelRepository
import com.example.bvk.database.packagedatabase.PackageRepository
import java.lang.IllegalArgumentException

class MandrelViewModelFactory(private val mandrelRepository: MandrelRepository, private val schemaRepository: PackageRepository,private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MandrelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MandrelViewModel(mandrelRepository, schemaRepository, context) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}