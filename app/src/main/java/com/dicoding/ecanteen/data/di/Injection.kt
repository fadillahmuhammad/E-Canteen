package com.dicoding.ecanteen.data.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.dicoding.ecanteen.data.UserRepository
import com.dicoding.ecanteen.data.local.pref.UserPreference
import com.dicoding.ecanteen.data.local.pref.dataStore
import com.dicoding.ecanteen.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class Injection private constructor(private val context: Context) {
    private val pref by lazy { UserPreference.getInstance(context.dataStore) }

    companion object {
        @Composable
        fun provideRepository(): UserRepository {
            val context = LocalContext.current
            val injection = remember { Injection(context) }
            return injection.provideRepository()
        }
    }

    @Composable
    fun provideRepository(): UserRepository {
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user?.token ?: "")
        return UserRepository.getInstance(pref, apiService)
    }
}