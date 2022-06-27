package com.example.bvk.database.mandreldatabase

import androidx.annotation.WorkerThread
import com.example.bvk.model.Mandrel
import kotlinx.coroutines.flow.Flow

class MandrelRepository(private val mandrelDao: MandrelDao) {

    val allMandrels: Flow<List<Mandrel>> = mandrelDao.getAllMandrels()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(mandrel: Mandrel) {
        mandrelDao.insert(mandrel)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(id: Int) {
        mandrelDao.deleteById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(mandrel: Mandrel) {
        mandrelDao.update(mandrel)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        mandrelDao.deleteAll()
    }
}