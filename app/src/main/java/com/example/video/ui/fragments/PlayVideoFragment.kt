package com.example.video.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.video.databinding.FragmentPlayVideoBinding

class PlayVideoFragment : Fragment() {

    private var _binding: FragmentPlayVideoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlayVideoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = PlayVideoFragmentArgs.fromBundle(requireArguments())
        val url = args.url

        val playerView = binding.videoPlayer
        val videoURI = Uri.parse(url)
        playerView.setVideoURI(videoURI)

        val mediaController = MediaController(context)
        mediaController.setMediaPlayer(playerView)
        playerView.setMediaController(mediaController)
        playerView.requestFocus()
        playerView.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}