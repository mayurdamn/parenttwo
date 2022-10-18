package com.alept.exampletwo.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alept.exampletwo.R

@Database(entities = [AllAppsTable::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun AppDao(): AppDao

    companion object {
        private val LOCK = Any()
        private var sInstance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    Log.d("LOG_TAG", "Creating new database instance")
                    sInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        context.getString(R.string.databaseName)
                    ).build()
                }
            }
            Log.d("LOG_TAG", "Getting the database instance")
            return sInstance
        }
    }

}
