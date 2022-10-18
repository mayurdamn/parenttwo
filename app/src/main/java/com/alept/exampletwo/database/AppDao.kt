package com.alept.exampletwo.database

import androidx.room.*
import androidx.room.OnConflictStrategy.ABORT

@Dao
interface AppDao {


    @Query("SELECT * FROM appsTables WHERE appName = :name")
    fun checkApp(name:String): List<AllAppsTable>

    @Query("SELECT * FROM appsTables")
    fun getApps() : List<AllAppsTable>

    @Query("SELECT * FROM appsTimer")
    fun getTimerValue() : List<AddTimer>

    @Insert(onConflict = ABORT)
    fun setBlockedApp(allAppsTable: AllAppsTable)

    @Insert(onConflict = ABORT)
    fun setIsBlock(addTimer: AddTimer)

    @Update
    fun updateBlockedApp(allAppsTable: AllAppsTable)

    @Delete
    fun removeBlockedApp(allAppsTable: AllAppsTable)

}