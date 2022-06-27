package com.example.bvk.database.mandreldatabase

import androidx.room.*
import com.example.bvk.model.Mandrel
import kotlinx.coroutines.flow.Flow

@Dao
interface MandrelDao {

    @Query("SELECT * FROM table_mandrel ORDER BY vertexDiameter ASC")
    fun getAllMandrels(): Flow<List<Mandrel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mandrel: Mandrel)

    @Update
    suspend fun update(mandrel: Mandrel)

    @Query("DELETE FROM table_mandrel")
    suspend fun deleteAll()

    @Query("DELETE FROM table_mandrel WHERE _id = :id")
    suspend fun deleteById(id: Int)
}