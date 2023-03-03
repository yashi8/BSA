package com.yashishu.bsa.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yashishu.bsa.databinding.FragmentCustomerSupportBinding

class CustomerSupport:Fragment() {
    private var _binding: FragmentCustomerSupportBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerSupportBinding.inflate(inflater,  container, false)
        auth = Firebase.auth
        db = Firebase.firestore
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnsubmit.setOnClickListener { submitForm() }
        }
    }

    private fun submitForm() {

    }

}


