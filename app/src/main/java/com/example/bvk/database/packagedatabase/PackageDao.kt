package com.example.bvk.database.packagedatabase

import androidx.room.*
import com.example.bvk.model.packageschema.PackageSchema
import kotlinx.coroutines.flow.Flow

@Dao
interface PackageDao {


    @Query("SELECT * FROM table_package_schema")
    fun getAlphabetizedWords(): Flow<List<PackageSchema>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(packageSchema: PackageSchema)

    @Update
    suspend fun update(packageSchema: PackageSchema)

    @Query("DELETE FROM table_package_schema")
    suspend fun deleteAll()

    @Query("DELETE FROM table_package_schema WHERE _id = :id")
    suspend fun deleteById(id: Int)
}