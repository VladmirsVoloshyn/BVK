package com.example.bvk

import android.app.Application
import com.example.bvk.database.mandreldatabase.MandrelRepository
import com.example.bvk.database.mandreldatabase.MandrelRoomDataBase
import com.example.bvk.database.packagedatabase.PackageRepository
import com.example.bvk.database.packagedatabase.PackageRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Suppress("unused")
class BVKApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val mandrelsDataBase by lazy {
        MandrelRoomDataBase.getDataBase(this, applicationScope)
    }

    val mandrelsRepository by lazy {
        MandrelRepository(mandrelsDataBase.mandrelDao())
    }

    private val schemasDataBase by lazy {
        PackageRoomDatabase.getDataBase(this, applicationScope)
    }

    val schemasRepository by lazy {
        PackageRepository(schemasDataBase.packageDao())
    }

    fun finish(){
        this.finish()
    }
}