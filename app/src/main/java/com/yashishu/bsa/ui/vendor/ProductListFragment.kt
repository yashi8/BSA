package com.yashishu.bsa.ui.vendor

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yashishu.bsa.databinding.FragmentProductListBinding

class ProductListFragment: Fragment() {
    private var _binding: FragmentProductListBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage



}