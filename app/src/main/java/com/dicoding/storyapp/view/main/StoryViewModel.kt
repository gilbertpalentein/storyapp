package com.dicoding.storyapp.view.main

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.Injection
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.model.UserStoryModel

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun listStories(): LiveData<PagingData<UserStoryModel>> =
        storyRepository.getStory().cachedIn(viewModelScope)

}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}