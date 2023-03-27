package com.yashishu.bsa.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.MainActivity
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentLoginSelectorBinding

class LoginSelectorFragment : Fragment() {

    private var _binding: FragmentLoginSelectorBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginSelectorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            adminlogin.setOnClickListener { gotoAdminScreen() }
            userlogin.setOnClickListener { gotoCustomerScreen() }
            vendorlogin.setOnClickListener { gotoVendorScreen() }
        }
    }

    private fun gotoVendorScreen() {
        findNavController().navigate(R.id.action_loginSelectorFragment_to_vndrLoginFragment)
    }

    private fun gotoCustomerScreen() {
        findNavController().navigate(R.id.action_loginSelectorFragment_to_custLoginFragment)
    }

    private fun gotoAdminScreen() {
        findNavController().navigate(R.id.action_loginSelectorFragment_to_admLoginFragment)
    }
}