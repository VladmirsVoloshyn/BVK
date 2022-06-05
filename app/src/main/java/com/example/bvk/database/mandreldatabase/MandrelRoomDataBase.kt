package com.example.bvk.database.mandreldatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bvk.model.Mandrel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Mandrel::class], version = 2, exportSchema = true)
abstract class MandrelRoomDataBase : RoomDatabase() {

        abstract fun mandrelDao(): MandrelDao

        private class MandrelsDataBaseCallBack(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { dataBase ->
                    scope.launch {
                        populateDatabase(dataBase.mandrelDao())
                    }
                }
            }

            suspend fun populateDatabase(mandrelDao : MandrelDao){
                mandrelDao.deleteAll()
            }
        }

        companion object {
            @Volatile
            private var INSTANCE: MandrelRoomDataBase? = null

            fun getDataBase(context: Context, scope: CoroutineScope): MandrelRoomDataBase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MandrelRoomDataBase::class.java,
                        "mandrels_database"
                    ).addCallback(MandrelsDataBaseCallBack(scope)).build()
                    INSTANCE = instance
                    instance
                }
            }
        }


}