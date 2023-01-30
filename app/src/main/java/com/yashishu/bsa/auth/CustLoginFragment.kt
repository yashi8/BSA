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
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.databinding.FragmentCustLoginBinding
import com.google.firebase.auth.ktx.auth
import com.yashishu.bsa.MainActivity
import com.yashishu.bsa.PrefUtil
import com.yashishu.bsa.R


class CustLoginFragment : Fragment() {

    private var _binding: FragmentCustLoginBinding? = null
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
        _binding = FragmentCustLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnLogin.setOnClickListener { verifyLogin() }
            textRegisterLink.setOnClickListener { findNavController().navigate(R.id.action_custLoginFragment_to_custRegisterFragment) }
        }
    }

    private fun verifyLogin() {
        val email = binding.editemail1.text.toString()
        val pwd = binding.editpassword1.text.toString()
        var error = false
        if (email.isEmpty()) {
            binding.editemail1.error = "Please provide an email address"
            error = true
        }
        if (!email.contains('@')) {
            binding.editemail1.error = "Please provide valid email"
            error = true
        }
        if (pwd.isEmpty()) {
            binding.editpassword1.error = "Please provide the password"
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
        takeCustomerToDashboard()
    }

    private fun getVerificationStatus(user: FirebaseUser?): Boolean {
        return true
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            takeCustomerToDashboard();
        }
    }

    private fun takeCustomerToDashboard() {
        PrefUtil(requireActivity()).setUserType(2)
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

}