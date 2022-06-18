package com.dicoding.storyapp.view.detailStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding
import com.dicoding.storyapp.model.UserStoryModel

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = "Story Details"

        showLoading(true)
        setupData()
    }

    private fun setupData() {
        val story = intent.getParcelableExtra<UserStoryModel>("Story") as UserStoryModel
        Glide.with(applicationContext)
            .load(story.photoUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(findViewById(R.id.detail_photo))
        binding.detailName.text = story.name
        binding.detailDesc.text = story.description
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}