package com.beardedhen.great_ix.configuration

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.beardedhen.great_ix.GreatIxApplication
import com.beardedhen.great_ix.logger.Logger
import java.util.*

/**
 * Helper/Base class for storing Share Preferences.
 */
@SuppressWarnings("unused", "WeakerAccess", "ConstantConditions")

open class ConfigurationBase {

    companion object {

        private fun getPreferences(): SharedPreferences? {
            try {
                return PreferenceManager.getDefaultSharedPreferences(GreatIxApplication.context)
            } catch (e: NullPointerException) {
                Logger.e("preferences null pointer", e)
                return null
            }

        }

        private fun getPreferencesEditor(): SharedPreferences.Editor {
            return getPreferences()!!.edit()
        }

        fun getString(key: String, defValue: String): String? {
            return getPreferences()!!.getString(key, defValue)
        }

        fun getString(key: String): String? {
            return getPreferences()!!.getString(key, null)
        }

        fun setString(key: String, value: String?) {
            getPreferencesEditor().putString(key, value).commit()
        }

        fun getInt(key: String, defValue: Int): Int {
            return getPreferences()!!.getInt(key, defValue)
        }

        fun getInt(key: String): Int {
            return getPreferences()!!.getInt(key, 0)
        }

        fun setInt(key: String, value: Int) {
            getPreferencesEditor().putInt(key, value).commit()
        }

        fun getLong(key: String, defValue: Long): Long {
            return getPreferences()!!.getLong(key, defValue)
        }

        fun getLong(key: String): Long {
            return getPreferences()!!.getLong(key, 0L)
        }

        fun getLongOptional(key: String): Long? {
            val value = getPreferences()!!.getLong(key, -1L)
            return if (value != -1L) value else null
        }

        fun setLong(key: String, value: Long?) {
            getPreferencesEditor().putLong(key, value ?: -1L).commit()
        }

        fun getBoolean(key: String, defValue: Boolean): Boolean {
            return getPreferences()!!.getBoolean(key, defValue)
        }

        fun getBoolean(key: String): Boolean {
            return getPreferences()!!.getBoolean(key, false)
        }

        fun setBoolean(key: String, value: Boolean) {
            getPreferencesEditor().putBoolean(key, value).commit()
        }

        fun getDate(key: String, defValue: Date?): Date? {
            val time = getPreferences()!!.getLong(key, -1L)
            return if (time == -1L) defValue else Date(time)
        }

        fun getDate(key: String): Date? {
            return getDate(key, null)
        }

        fun setDate(key: String, value: Date?) {
            var time = -1L
            if (value != null) {
                time = value.time
            }

            getPreferencesEditor().putLong(key, time).commit()
        }
    }
}