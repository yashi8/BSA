package com.yashishu.bsa.ui.vendor

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
import com.google.firebase.storage.ktx.storage
import com.yashishu.bsa.databinding.FragmentAddProductBinding
import com.yashishu.bsa.databinding.FragmentViewProductsBinding

class ViewProductsFragment: Fragment() {
    private var _binding: FragmentViewProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewProductsBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage
        return binding.root
    }

    companion object {
        const val REQUEST_IMAGE_GET = 12
        const val COLL_PRODUCT = "products"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}