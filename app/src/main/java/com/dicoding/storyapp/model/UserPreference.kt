package com.dicoding.storyapp.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUserData(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?:"",
                preferences[USERID_KEY] ?:"",
                preferences[TOKEN_KEY] ?: "",
                preferences[LAT_KEY] ?: 0.0,
                preferences[LON_KEY] ?: 0.0,
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveLoginUserData(authToken: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = authToken.name
            preferences[USERID_KEY] = authToken.userId
            preferences[TOKEN_KEY] = authToken.token
            preferences[LAT_KEY] = authToken.lat
            preferences[LON_KEY] = authToken.lon
            preferences[STATE_KEY] = authToken.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(STATE_KEY)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val USERID_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val LAT_KEY = doublePreferencesKey("lat")
        private val LON_KEY = doublePreferencesKey("lon")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}