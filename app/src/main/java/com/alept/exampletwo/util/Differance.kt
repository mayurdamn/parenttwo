package com.alept.exampletwo.util

import java.sql.Time

object Differance {

    fun difference(start: Time, stop: Time): Time {
        val diff = Time(0, 0, 0)

        if (stop.seconds > start.seconds) {
            --start.minutes
            start.seconds += 60
        }

        diff.seconds = start.seconds - stop.seconds
        if (stop.minutes > start.minutes) {
            --start.hours
            start.minutes += 60
        }

        diff.minutes = start.minutes - stop.minutes
        diff.hours = start.hours - stop.hours

        return diff
    }
}