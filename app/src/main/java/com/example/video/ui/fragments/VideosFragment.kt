package com.example.video.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.video.data.User
import com.example.video.data.Video
import com.example.video.databinding.FragmentVideosBinding
import com.example.video.databinding.VideoItemLayoutBinding
import com.example.video.ui.adapter.VideoViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VideosFragment : Fragment() {

    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAuth: FirebaseAuth
    private var user: User? = null
    private var addPoints = 10
    private var addLevel = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()

        binding.rvVideos.apply {
            set3DItem(true)
            setAlpha(true)
            setInfinite(true)
        }

        getScore()

        val options = FirebaseRecyclerOptions.Builder<Video>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("videos"), Video::class.java)
            .build()

        val firebaseAdapter = object : FirebaseRecyclerAdapter<Video, VideoViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
                val layoutView = VideoItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return VideoViewHolder(layoutView)
            }

            override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: Video) {
                activity?.let {
                    holder.prepareExoPlayer(
                        it.application,
                        model.title,
                        model.genre,
                        model.url,
                        context
                    )
                }
                holder.itemView.setOnClickListener {
                    updateScore(model.url)
                }
            }
        }
        firebaseAdapter.startListening()
        binding.rvVideos.adapter = firebaseAdapter
    }

    private fun updateScore(url: String?) {
        if (user != null) {
            user?.points = user?.points?.plus(addPoints)
            user?.level = user?.level?.plus(addLevel)
        }
        val uid = mAuth.currentUser?.uid!!
        FirebaseDatabase.getInstance().getReference("users").child(uid)
            .setValue(user).addOnSuccessListener {
                val action =
                    DashBoardFragmentDirections.actionDashBoardFragmentToPlayVideoFragment(url.toString())
                findNavController().navigate(action)
            }
    }


    private fun getScore() {
        val uid = mAuth.currentUser?.uid!!
        val db = FirebaseDatabase.getInstance().getReference("users")
        db.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue((User::class.java))
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}