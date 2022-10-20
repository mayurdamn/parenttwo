package com.alept.exampletwo.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "appsTimer")
class AddTimer {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var timerAllowed : Int

    constructor(id: Int, timerAllowed: Int) {
        this.id = id
        this.timerAllowed = timerAllowed

    }

}