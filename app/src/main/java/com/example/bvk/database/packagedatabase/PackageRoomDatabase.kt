package com.example.bvk.database.packagedatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bvk.model.packageschema.PackageSchema
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [PackageSchema::class], version = 2, exportSchema = true)

abstract class PackageRoomDatabase : RoomDatabase() {

    abstract fun packageDao(): PackageDao

    private class SchemasDataBaseCallBack(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {dataBase ->
                scope.launch {
                    populateDatabase(dataBase.packageDao())
                }
            }
        }

        suspend fun populateDatabase(schemasDao: PackageDao){
            schemasDao.deleteAll()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PackageRoomDatabase? = null

        fun getDataBase(context: Context, scope: CoroutineScope): PackageRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PackageRoomDatabase::class.java,
                    "schemas_database"
                ).addCallback(SchemasDataBaseCallBack(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}