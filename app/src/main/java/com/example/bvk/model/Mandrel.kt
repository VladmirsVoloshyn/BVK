package com.example.bvk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(tableName = "table_mandrel")
data class Mandrel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int = 0,
    @ColumnInfo(name = "name") var mandrelName : String = "",
    @ColumnInfo(name = "vertexDiameter") val vertexDiameter: Double = 0.0,
    @ColumnInfo(name = "baseDiameter") val baseDiameter: Double = 0.0,
    @ColumnInfo(name = "height") var height: Int = 0,
    @ColumnInfo(name = "infelicity") val infelicityCoefficient : Double = 1.0120,
    @ColumnInfo(name = "max_infelicity_height") val maxInfelicityHeight : Int = 30,
    var tapper: Double = 0.0,
    var membraneWight: Double = 0.0,
    var adhesiveSleeveWeight: Double = 0.0,
    var totalMembraneLength : Double = 0.0,
    var recommendedAdhesiveSleeveWeight : Double = 0.0
) {
    fun getSizeIdentifier(): String {
        return this.mandrelName.substring(0..1)
    }

    override fun hashCode(): Int {
        return ((vertexDiameter*1000.0) + (baseDiameter*1000) / height).toInt()
    }
}