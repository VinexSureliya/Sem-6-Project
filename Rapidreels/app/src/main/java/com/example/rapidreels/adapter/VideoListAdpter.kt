package com.example.rapidreels.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rapidreels.ProfileActivity
import com.example.rapidreels.R
import com.example.rapidreels.databinding.VideoItemRowBinding
import com.example.rapidreels.model.UserModel
import com.example.rapidreels.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VideoListAdpter(
    options:FirestoreRecyclerOptions<VideoModel>
): FirestoreRecyclerAdapter<VideoModel,VideoListAdpter.VideoViewHolder>(options) {

    private var isLiked = false
    lateinit var adpder : VideoListAdpter

    inner class VideoViewHolder(private val binding: VideoItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindvideo(videoModel: VideoModel) {
            //bindUserData
            Firebase.firestore.collection("users")
                .document(videoModel.uploaderId)
                .get().addOnSuccessListener {
                    val userModel = it?.toObject(UserModel::class.java)
                    userModel?.apply {
                        binding.usernameView.text = username

                        //bind profilepic
                        Glide.with(binding.profileIcon).load(profilePic)
                            .circleCrop()
                            .apply(
                                RequestOptions().placeholder(R.drawable.icon_profile)
                            )
                            .into(binding.profileIcon)

                        binding.userDetailLayout.setOnClickListener{
                            val intent = Intent(binding.userDetailLayout.context, ProfileActivity::class.java)
                            intent.putExtra("profile_user_id", id)
                            binding.userDetailLayout.context.startActivity(intent)
                        }
                    }
                }
            binding.captionView.text = videoModel.title
            binding.progressBar.visibility = android.view.View.VISIBLE

            //Like Reels
            binding.captionView.text = videoModel.title
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.likeBtn.setOnClickListener {
                if (isLiked) {
                    binding.likeBtn.setImageResource(R.drawable.heart)
                } else {
                    binding.likeBtn.setImageResource(R.drawable.like)
                }
                isLiked = !isLiked
            }

            //bindvideo
            binding.videoView.apply {
                setVideoPath(videoModel.url)
                setOnPreparedListener {
                    binding.progressBar.visibility = android.view.View.GONE
                    it.start()
                    it.isLooping = true
                }
                //play pause
                setOnClickListener {
                    if (isPlaying){
                        pause()
                        binding.pauseIcon.visibility = android.view.View.VISIBLE
                    }else{
                        start()
                        binding.pauseIcon.visibility = android.view.View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = VideoItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: VideoModel) {
        holder.bindvideo(model)
    }
}