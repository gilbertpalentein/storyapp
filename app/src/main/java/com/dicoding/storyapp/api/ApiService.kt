package com.dicoding.storyapp.api

import com.dicoding.storyapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getListStoryLocation(
        @Header("Authorization") authToken: String,
        @Query("location") location: Int = 1
    ): Call<ListStoryResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") authToken: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<UploadPhotoResponse>

    @GET("stories")
    suspend fun getListStory(
        @Header("Authorization") authToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ListStoryResponse
}
