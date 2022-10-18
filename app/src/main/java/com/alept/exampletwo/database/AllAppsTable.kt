package com.alept.exampletwo.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "appsTables")
class AllAppsTable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var appName : String
    var packageName : String
    var isBlocked : Int
    var timerAllowed : Int = 2

    constructor(id: Int, appName: String, packageName: String, isBlocked: Int) {
        this.id = id
        this.appName = appName
        this.packageName = packageName
        this.isBlocked = isBlocked

    }

    @Ignore
    constructor(appName: String, packageName: String, isBlocked: Int) {
        this.appName = appName
        this.packageName = packageName
        this.isBlocked = isBlocked

    }
    @Ignore
    constructor(appName: String, packageName: String, isBlocked: Int,timerAllowed:Int) {
        this.appName = appName
        this.packageName = packageName
        this.isBlocked = isBlocked
        this.timerAllowed= timerAllowed
    }



}