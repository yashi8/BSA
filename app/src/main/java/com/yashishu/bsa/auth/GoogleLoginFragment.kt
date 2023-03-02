package com.yashishu.bsa.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.yashishu.bsa.databinding.FragmentGoogleLogin2Binding

class GoogleLoginFragment {
    private var _binding: FragmentGoogleLogin2Binding? = null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth
    private lateinit var goggleSignInClient: GoogleSignInClient


   //override
   fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoogleLogin2Binding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
    }
}
