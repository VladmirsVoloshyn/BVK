package com.example.bvk

import android.app.Application
import com.example.bvk.database.MandrelRepository
import com.example.bvk.database.MandrelRoomDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Suppress("unused")
class BVKApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val dataBase by lazy {
        MandrelRoomDataBase.getDataBase(this, applicationScope)
    }
    val repository by lazy {
        MandrelRepository(dataBase.mandrelDao())
    }
}