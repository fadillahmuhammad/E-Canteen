package com.dicoding.ecanteen.data.local.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSessionPembeli(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id
            preferences[MESSAGE_KEY] = user.message
            preferences[FULLNAME_KEY] = user.fullName
            preferences[TELP_KEY] = user.telp
            preferences[USERNAME_KEY] = user.username
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            preferences[STORENAME_KEY] = user.storeName
            preferences[DESC_KEY] = user.desc
            preferences[USER_TYPE_KEY] = "pembeli"
        }
        Log.d("UserPreference", "Saved user: $user")
    }

    suspend fun saveSessionPenjual(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id
            preferences[MESSAGE_KEY] = user.message
            preferences[FULLNAME_KEY] = user.fullName
            preferences[TELP_KEY] = user.telp
            preferences[USERNAME_KEY] = user.username
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            preferences[STORENAME_KEY] = user.storeName
            preferences[DESC_KEY] = user.desc
            preferences[USER_TYPE_KEY] = "penjual"
        }
        Log.d("UserPreference", "Saved user: $user")
    }

    fun getSessionPenjual(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val userModel = UserModel(
                preferences[ID_KEY] ?: 0,
                preferences[MESSAGE_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[FULLNAME_KEY] ?: "",
                preferences[TELP_KEY] ?: "",
                preferences[USERNAME_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[STORENAME_KEY] ?: "",
                preferences[DESC_KEY] ?: "",
                preferences[USER_TYPE_KEY] ?: ""
            )
            Log.d("UserPreference", "Fetched user: $userModel")
            userModel
        }
    }

    fun getSessionPembeli(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val userModel = UserModel(
                preferences[ID_KEY] ?: 0,
                preferences[MESSAGE_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[FULLNAME_KEY] ?: "",
                preferences[TELP_KEY] ?: "",
                preferences[USERNAME_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[STORENAME_KEY] ?: "",
                preferences[DESC_KEY] ?: "",
                preferences[USER_TYPE_KEY] ?: ""
            )
            Log.d("UserPreference", "Fetched user: $userModel")
            userModel
        }
    }

    fun getSession(): Flow<UserModel?> {
        return dataStore.data.map { preferences ->
            val isLogin = preferences[IS_LOGIN_KEY] ?: false
            if (isLogin) {
                UserModel(
                    preferences[ID_KEY] ?: 0,
                    preferences[MESSAGE_KEY] ?: "",
                    preferences[TOKEN_KEY] ?: "",
                    preferences[FULLNAME_KEY] ?: "",
                    preferences[TELP_KEY] ?: "",
                    preferences[USERNAME_KEY] ?: "",
                    isLogin,
                    preferences[STORENAME_KEY] ?: "",
                    preferences[DESC_KEY] ?: "",
                    preferences[USER_TYPE_KEY] ?: ""
                )
            } else {
                null
            }
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID_KEY = intPreferencesKey("id")
        private val MESSAGE_KEY = stringPreferencesKey("message")
        private val FULLNAME_KEY = stringPreferencesKey("fullName")
        private val TELP_KEY = stringPreferencesKey("telp")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val STORENAME_KEY = stringPreferencesKey("storeName")
        private val DESC_KEY = stringPreferencesKey("desc")
        private val USER_TYPE_KEY = stringPreferencesKey("userType")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}