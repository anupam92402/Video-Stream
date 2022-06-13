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
import com.example.video.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.actionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }

        binding.btnSubmit.setOnClickListener {
            loginUser()
        }

    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user != null) {
            findNavController().navigate(R.id.action_loginFragment_to_dashBoardFragment)
        }
    }

    private fun loginUser() {
        val email = binding.tilEmail.editText?.text.toString()
        val password = binding.tilPassword.editText?.text.toString()
        binding.progressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.INVISIBLE
                binding.tilEmail.editText?.text?.clear()
                binding.tilPassword.editText?.text?.clear()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    findNavController().navigate(R.id.action_loginFragment_to_dashBoardFragment)
                    Toast.makeText(context, "User Logged In Successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("Error in Logging", "${task.exception} ")
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}