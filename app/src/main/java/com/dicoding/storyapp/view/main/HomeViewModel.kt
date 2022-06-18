package com.dicoding.storyapp.view.main

import android.service.controls.ControlsProviderService
import android.util.Log
import androidx.lifecycle.*
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.model.ListStoryResponse
import com.dicoding.storyapp.model.UserModel
import com.dicoding.storyapp.model.UserPreference
import com.dicoding.storyapp.model.UserStoryModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: UserPreference) : ViewModel() {
    private val listStory = MutableLiveData<ArrayList<UserStoryModel>>()

    fun getUser(): LiveData<UserModel> {
        return pref.getUserData().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun setListStoryWithLoc(authToken: String) {
        ApiConfig.getApiService().getListStoryLocation(authToken).enqueue(object :
            Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {
                    listStory.postValue(response.body()?.listStory)
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                Log.e(ControlsProviderService.TAG, "Failure: ${t.message}")
            }

        })
    }

    fun getListStory(): LiveData<ArrayList<UserStoryModel>> {
        return listStory
    }

}