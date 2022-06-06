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
    @ColumnInfo(name = "cap_amount_in_box") val capAmountInBox: Int = 0,
    @ColumnInfo(name = "cap_amount_in_bundle") val capAmountInBundle: Int = 0
)