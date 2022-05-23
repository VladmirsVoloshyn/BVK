package com.example.bvk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_mandrel")
data class Mandrel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int = 0,
    @ColumnInfo(name = "name") var mandrelName : String = "",
    @ColumnInfo(name = "vertexDiameter") val vertexDiameter: Double = 0.0,
    @ColumnInfo(name = "baseDiameter") val baseDiameter: Double = 0.0,
    @ColumnInfo(name = "height") var height: Int = 0,
    @ColumnInfo(name = "infelicity") val infelicity : Double =0.0,
    var tapper: Double = 0.0,
    var membraneWight: Double = 0.0,
    var adhesiveSleeveWeight: Double = 0.0,
    var totalMembraneLength : Double = 0.0,
)