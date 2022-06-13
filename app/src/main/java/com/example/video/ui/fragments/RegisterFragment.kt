package com.example.video.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.video.R
import com.example.video.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvAlreadyRegistered.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnSubmit.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        mAuth = FirebaseAuth.getInstance()
        val email = binding.tilEmail.editText?.text.toString()
        val password = binding.tilPassword.editText?.text.toString()
        binding.progressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.INVISIBLE
                binding.tilEmail.editText?.text?.clear()
                binding.tilPassword.editText?.text?.clear()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val action =
                        RegisterFragmentDirections.actionRegisterFragmentToOnBoardingFragment(email)
                    findNavController().navigate(action)
                    Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("Error in Registering", "${task.exception} ")
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}