package com.beardedhen.great_ix.configuration

import com.beardedhen.great_ix.BuildConfig

open class Configuration : ConfigurationBase() {


    companion object {

        private const val PREVIOUS_PASSWORD_KEY = BuildConfig.APPLICATION_ID + "PREVIOUS_PASSWORD_KEY"
        private const val IS_SETTINGS_INCOMPLETE_KEY = BuildConfig.APPLICATION_ID + "IS_SETTINGS_COMPLETE_KEY"

        private const val YOUR_NAME_KEY = BuildConfig.APPLICATION_ID + "YOUR_NAME_KEY"
        private const val YOUR_WORK_PLACE_KEY = BuildConfig.APPLICATION_ID + "YOUR_WORK_PLACE_KEY"
        private const val YOUR_OTHER_WORK_PLACE_KEY = BuildConfig.APPLICATION_ID + "YOUR_OTHER_WORK_PLACE_KEY"
        private const val YOUR_ROLE_KEY = BuildConfig.APPLICATION_ID + "YOUR_ROLE_KEY"
        private const val YOUR_OTHER_ROLE_KEY = BuildConfig.APPLICATION_ID + "YOUR_OTHER_ROLE_KEY"
        private const val YOUR_EMAIL_KEY = BuildConfig.APPLICATION_ID + "YOUR_EMAIL_KEY"

        private var configuration: Configuration? = null

        fun getInstance(): Configuration {
            configuration = configuration ?: Configuration()
            return configuration!!
        }
    }


    fun getSetting( key: String): String {
        return getString( key ) ?:""
    }
    fun storeSetting( key: String, value: String) {
        return setString( key, value )
    }
    fun getSettingFlag( key: String): Boolean{
        return getBoolean( key, true )
    }
    fun storeSettingFlag( key: String, value: Boolean) {
        return setBoolean( key, value )
    }


    open var previousPassword: String
        get() = getString(PREVIOUS_PASSWORD_KEY,"") ?:""
        set(value) = setString(PREVIOUS_PASSWORD_KEY, value)

    open var isSettingsIncomplete: Boolean
        get() = getBoolean(IS_SETTINGS_INCOMPLETE_KEY,true )
        set(value) = setBoolean(IS_SETTINGS_INCOMPLETE_KEY, value)

    open var yourName: String
        get() = getString(YOUR_NAME_KEY,"") ?:""
        set(value) = setString(YOUR_NAME_KEY, value)

    open var yourPlaceOfWork: String
        get() = getString(YOUR_WORK_PLACE_KEY,"") ?:""
        set(value) = setString(YOUR_WORK_PLACE_KEY, value)

    open var yourOtherPlaceOfWork: String
        get() = getString(YOUR_OTHER_WORK_PLACE_KEY,"") ?:""
        set(value) = setString(YOUR_OTHER_WORK_PLACE_KEY, value)

    open var yourRole: String
        get() = getString(YOUR_ROLE_KEY,"") ?:""
        set(value) = setString(YOUR_ROLE_KEY, value)

    open var yourOtherRole: String
        get() = getString(YOUR_OTHER_ROLE_KEY,"") ?:""
        set(value) = setString(YOUR_OTHER_ROLE_KEY, value)

    open var yourEmail: String
        get() = getString(YOUR_EMAIL_KEY,"") ?:""
        set(value) = setString(YOUR_EMAIL_KEY, value)
}