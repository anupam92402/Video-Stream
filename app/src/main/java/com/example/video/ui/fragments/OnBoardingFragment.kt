package com.example.video.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.video.R
import com.example.video.data.User
import com.example.video.databinding.FragmentOnBoardingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAuth: FirebaseAuth
    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        activity?.actionBar?.hide()
        val args = OnBoardingFragmentArgs.fromBundle(requireArguments())
        email = args.email
        binding.btnRegister.setOnClickListener {
            submitUserDetails()
        }
    }

    private fun submitUserDetails() {
        uploadToFirebase()
        findNavController().navigate(R.id.action_onBoardingFragment_to_dashBoardFragment)
    }

    private fun uploadToFirebase() {
        val userId = mAuth.currentUser?.uid.toString()
        val db = FirebaseDatabase.getInstance().getReference("users")
        val name = binding.tilName.editText?.text.toString()
        val number = binding.tilMobile.editText?.text.toString()
        val userData = User(name, number, email, "bronze", 0, 1)
        db.child(userId).setValue(userData)
        binding.tilName.editText?.text?.clear()
        binding.tilMobile.editText?.text?.clear()
        Toast.makeText(context, "Data Uploaded", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}