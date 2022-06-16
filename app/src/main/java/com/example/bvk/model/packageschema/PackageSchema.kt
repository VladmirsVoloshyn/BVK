package com.example.bvk.model.packageschema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_package_schema")
data class PackageSchema(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int = 0,
    @ColumnInfo(name = "schema_name") val schemaName: String = "",
    @ColumnInfo(name = "box_type") val boxType: String = "",
    @ColumnInfo(name = "first_line_count") val firstLineCount: Int = 0,
    @ColumnInfo(name = "second_line_count") val secondLineCount: Int = 0,
    @ColumnInfo(name = "is_straight_laying") val isStraightLaying: Boolean = false,
    @ColumnInfo(name = "cap_amount_in_bundle") val capAmountInBundle: Int = 0,
    @ColumnInfo(name = "layer_count") val layerCount: Int = 0,
    @ColumnInfo(name = "cap_vertex_diameter") val capVertexDiameter: Int = 0,
    @ColumnInfo(name = "cap_height") val capHeight: Int = 0,
    var capAmountInBox: Int = 0
) {
    init {
        if (firstLineCount == secondLineCount) {
            capAmountInBox = (firstLineCount*capAmountInBundle) * layerCount
        }else if(firstLineCount!=secondLineCount){
            if (layerCount % 2 == 0){
                capAmountInBox = ((firstLineCount*capAmountInBundle) * layerCount) - ((layerCount/2) * capAmountInBundle)
            } else if(layerCount % 2 == 1) {
                capAmountInBox = ((firstLineCount*capAmountInBundle) * layerCount) - (((layerCount - 1)/2) * capAmountInBundle)
            }
        }
    }
}