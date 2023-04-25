package com.yashishu.bsa.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.MainActivity
import com.yashishu.bsa.PrefUtil
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentVndrLoginBinding

class VndrLoginFragment : Fragment() {

    private var _binding: FragmentVndrLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVndrLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnvndLogin.setOnClickListener { verifyLogin() }
            vndRegisterLink.setOnClickListener { findNavController().navigate(R.id.action_vndrLoginFragment_to_vndrRegisterFragment) }
            imgPhone2.setOnClickListener { findNavController().navigate(R.id.action_vndrLoginFragment_to_phoneLogin) }
            imgGoogle2.setOnClickListener { findNavController().navigate(R.id.action_vndrLoginFragment_to_googleLoginFragment) }

        }


    }

    private fun verifyLogin() {
        // TODO("Not yet implemented")

        val email = binding.veditemail.text.toString()
        val pwd = binding.veditpassword.text.toString()
        var error = false
        if (email.isEmpty()) {
            binding.veditemail.error = "Please provide an email address"
            error = true
        }
        if (!email.contains('@')) {
            binding.veditemail.error = "Please provide valid email"
            error = true
        }
        if (pwd.isEmpty()) {
            binding.veditpassword.error = "Please provide the password"
            error = true
        }
        if (error) {
            return
        }
        auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUI(user)
            } else {
                Toast.makeText(activity, "Error ${task.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
                updateUI(null)
            }
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (!getVerificationStatus(user)) {
            Toast.makeText(activity, "Your account is not verified yet", Toast.LENGTH_SHORT).show()
            return
        }
        takeVendorToDashboard()
    }

    private fun getVerificationStatus(user: FirebaseUser?): Boolean {
        return true
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            takeVendorToDashboard()
        }
    }

    private fun takeVendorToDashboard() {
        PrefUtil(requireActivity()).setUserType(1).apply {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }

    }

}