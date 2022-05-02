package com.example.bvk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_mandrel")
data class Mandrel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int,
    @ColumnInfo(name = "vertexDiameter")  val vertexDiameter: Double,
    @ColumnInfo(name = "baseDiameter")val baseDiameter: Double,
    @ColumnInfo(name = "height")val height: Double,
) {
}