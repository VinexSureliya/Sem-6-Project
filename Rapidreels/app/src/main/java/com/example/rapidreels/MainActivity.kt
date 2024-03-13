package com.example.rapidreels

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rapidreels.adapter.VideoListAdpter
import com.example.rapidreels.databinding.ActivityMainBinding
import com.example.rapidreels.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var adpder : VideoListAdpter
    //private var isLiked = false

    private lateinit var database: DatabaseReference
    private val storage = Firebase.storage
    private val storageRef = storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavBar.setOnItemSelectedListener {menuItem->
            when(menuItem.itemId){
                R.id.bottom_menu_home->{
                    //Goto home activity
                    startActivity(Intent(this,MainActivity::class.java))
                }

                R.id.bottom_menu_add_video->{
                    //Goto upload video activity
                    startActivity(Intent(this,VideoUploadActivity::class.java))
                }

                R.id.bottom_menu_profile->{
                    //Goto profileActivity
                    val intent = Intent(this,ProfileActivity::class.java)
                    intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)
                }
            }
            false
        }

        binding.shareBtn.setOnClickListener {

            val videoUri = "https://firebasestorage.googleapis.com/v0/b/rapidreels-7e588.appspot.com/o/videos%2F351140648?alt=media&token=62b62656-621c-4576-ba70-2c094b06e2e3"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "video/.mp4"
                putExtra(Intent.EXTRA_STREAM, Uri.parse(videoUri))
            }
            startActivity(Intent.createChooser(intent,"Share Via"))
        }
        setupViewPager()
    }

    private fun setupViewPager(){
        val options = FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(
                Firebase.firestore.collection("videos"),
                VideoModel::class.java
            ).build()
        adpder = VideoListAdpter(options)
        binding.viewPager.adapter = adpder
    }

    override fun onStart() {
        super.onStart()
        adpder.startListening()
    }

    override fun onStop() {
        super.onStop()
        adpder.startListening()
    }
}