package com.example.video.ui.adapter

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.example.video.databinding.VideoItemLayoutBinding

class VideoViewHolder(private val binding: VideoItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun prepareExoPlayer(
        application: Application,
        title: String?,
        genre: String?,
        url: String?,
        context: Context?
    ) {
        binding.videoName.text = title
        binding.videoGenre.text = genre
        val playerView = binding.playerView
        val videoURI = Uri.parse(url)
        playerView.setVideoURI(videoURI)

        val mediaController = MediaController(context)
        mediaController.setMediaPlayer(playerView)
        playerView.setMediaController(mediaController)
        playerView.requestFocus()
    }


}

