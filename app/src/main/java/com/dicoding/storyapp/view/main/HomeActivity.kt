package com.dicoding.storyapp.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.adapter.StoryAdapter
import com.dicoding.storyapp.camera.CameraActivity
import com.dicoding.storyapp.model.UserPreference
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.welcome.WelcomeActivity
import android.provider.Settings
import com.dicoding.storyapp.adapter.LoadingStateAdapter
import com.dicoding.storyapp.data.SharedData
import com.dicoding.storyapp.databinding.ActivityHomeBinding
import com.dicoding.storyapp.maps.MapsActivity
import com.google.android.gms.maps.model.LatLng

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: StoryAdapter
    private var listStoryMap: ArrayList<LatLng>? = null
    private var listStoryMapName: ArrayList<String>? = null
    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = "Story List"

        setupViewModel()
        setupAction()
        setupLanguage()
        playAnimation()
    }

    private fun setupLanguage() {
        binding.settingLanguange.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setupViewModel() {
        showLoading(true)
        adapter = StoryAdapter()
        listStoryMap = ArrayList()
        listStoryMapName = ArrayList()

        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[HomeViewModel::class.java]

        homeViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                SharedData.token = "Bearer ${user.token}"
                binding.titleTextView.text = getString(R.string.title_home, user.name)
                binding.apply {
                    if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        rvUsers.layoutManager = GridLayoutManager(this@HomeActivity, 2)
                    } else {
                        rvUsers.layoutManager = LinearLayoutManager(this@HomeActivity)
                    }
                    showLoading(true)
                    rvUsers.adapter = adapter.withLoadStateFooter(
                        footer = LoadingStateAdapter {
                            adapter.retry()
                        }
                    )
                    setToken("Bearer ${user.token}")
                }
                storyViewModel.listStories().observe(this) { story ->
                    adapter.submitData(lifecycle, story)
                    showLoading(false)
                }
            } else {
                showLoading(false)
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        homeViewModel.getListStory().observe(this, {
            if (it != null) {
                for (i in it.indices) {
                    listStoryMap!!.add(LatLng(it[i].lat, it[i].lon))
                    listStoryMapName!!.add(it[i].name)
                }
            }
        })
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            homeViewModel.logout()
        }
    }

    private fun setToken(token: String) {
        binding.apply {
            if (token.isEmpty()) return
            showLoading(true)
            homeViewModel.setListStoryWithLoc(token)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val actionbar = supportActionBar
        actionbar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000000")))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_story -> {
                Intent(this, CameraActivity::class.java).also {
                    it.putExtra(TOKEN_KEY, SharedData.token)
                    startActivity(it)
                }
            }
            R.id.story_maps -> {
                Intent(this, MapsActivity::class.java).also {
                    it.putExtra(LIST_MAP_KEY, listStoryMap)
                    it.putExtra(LIST_MAP_NAME_KEY, listStoryMapName)
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun playAnimation() {
        val title =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, logout)
            startDelay = 300
        }.start()
    }

    companion object {
        const val TOKEN_KEY = "TOKEN"
        const val LIST_MAP_KEY = "MAP"
        const val LIST_MAP_NAME_KEY = "NAME"
    }
}

