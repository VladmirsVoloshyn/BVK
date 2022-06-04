package com.example.bvk.database.packagedatabase

import androidx.annotation.WorkerThread
import com.example.bvk.model.packageschema.PackageSchema
import kotlinx.coroutines.flow.Flow

class PackageRepository(private val packageDao: PackageDao) {

    val allSchemas: Flow<List<PackageSchema>> = packageDao.getAlphabetizedWords()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(packageSchema: PackageSchema) {
        packageDao.insert(packageSchema)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(id: Int) {
        packageDao.deleteById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(packageSchema: PackageSchema) {
        packageDao.update(packageSchema)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        packageDao.deleteAll()
    }
}