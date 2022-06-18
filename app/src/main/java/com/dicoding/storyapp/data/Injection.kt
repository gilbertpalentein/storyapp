package com.dicoding.storyapp.data

import com.dicoding.storyapp.api.ApiConfig

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}