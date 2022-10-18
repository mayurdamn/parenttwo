package com.alept.exampletwo.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


class SharePreferences(appContext: Context) {

    companion object {
        const val COUMT_KEY = ""
        const val DEFAULT_STRING_VALUE = ""
        const val DEFAULT_INT_VALUE = -1
        const val DEFAULT_LONG_VALUE = -1
        const val DEFAULT_BOOLEAN_VALUE = false
        const val IS_ALLOWED_TIME = false
        const val ALLOWED_TIME = ""
        const val DEFAULT_ALLOWED_TIME = 10

        var Count = 0

    }

    private var sharedpreferences: SharedPreferences? = null
    private val PREF_NAME = "ParentsApp"

    init {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(appContext)
        sharedpreferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    //put data functions

    fun putString(key: String?, value: String?) {
        if (key == null) {
            throw NullPointerException()
        }
        if (value == null) {
            throw NullPointerException()
        }
        sharedpreferences?.let {
            with(it.edit()) {
                putString(key, value)
                apply()
            }
        }
    }

    fun putInt(key: String?, value: Int?) {
        if (key == null) {
            throw NullPointerException()
        }
        if (value == null) {
            throw NullPointerException()
        }
        sharedpreferences?.let {
            with(it.edit()) {
                putInt(key, value)
                apply()
            }
        }
    }

    fun putLong(key: String?, value: Long?) {
        if (key == null) {
            throw NullPointerException()
        }
        if (value == null) {
            throw NullPointerException()
        }
        sharedpreferences?.let {
            with(it.edit()) {
                putLong(key, value)
                apply()
            }
        }
    }

    fun putBoolean(key: String?, value: Boolean) {
        if (key == null) {
            throw NullPointerException()
        }
        sharedpreferences?.let {
            with(it.edit()) {
                putBoolean(key, value)
                apply()
            }
        }
    }

    //get data functions

    fun getString(key: String): String {
        return sharedpreferences!!.getString(key, DEFAULT_STRING_VALUE)!!
    }

    fun getInt(key: String): Int {
        return sharedpreferences!!.getInt(key, DEFAULT_INT_VALUE)
    }

    fun getLong(key: String): Long {
        return sharedpreferences!!.getLong(key, DEFAULT_LONG_VALUE.toLong())
    }

    fun getBoolean(key: String): Boolean {
        return sharedpreferences!!.getBoolean(key, DEFAULT_BOOLEAN_VALUE)
    }

    // clear data functions

    fun clearAllPrefData() {
        sharedpreferences?.let {
            with(it.edit()) {
                clear()
                apply()
            }
        }
    }

     fun removeBadKeys(key :String) {

        val editor = sharedpreferences!!.edit()
        for (key in sharedpreferences!!.all.keys) {
            if (key.startsWith(key)) {
                editor.remove(key)
            }
        }
        editor.commit()
    }

  /*  private fun clearUsersData() {
        putLong(USER_ID, DEFAULT_LONG_VALUE.toLong())
        putString(AUTH_TOKEN, DEFAULT_STRING_VALUE)
    }

    private fun clearAuthPrefData() {
        putBoolean(IS_LOGGED_IN, DEFAULT_BOOLEAN_VALUE)
        putBoolean(IS_CATEGORY_SUBMITTED, DEFAULT_BOOLEAN_VALUE)
    }*/

   /* fun logout() {
        clearUsersData()
        clearAuthPrefData()
    }*/
}