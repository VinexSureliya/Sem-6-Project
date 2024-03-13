package com.example.rapidreels

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rapidreels.adapter.VideoListAdpter
import com.example.rapidreels.databinding.ActivitySingleVideoPlayerBinding
import com.example.rapidreels.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SingleVideoPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivitySingleVideoPlayerBinding
    lateinit var videoId: String
    lateinit var adapter:VideoListAdpter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivitySingleVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoId = intent.getStringExtra("videoId")!!
        setupViewPager()
    }

    fun setupViewPager(){
        val options = FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(
                Firebase.firestore.collection("videos")
                    .whereEqualTo("videoId",videoId),
                VideoModel::class.java
            ).build()
        adapter = VideoListAdpter(options)
        binding.viewPager.adapter = adapter
    }
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }
}