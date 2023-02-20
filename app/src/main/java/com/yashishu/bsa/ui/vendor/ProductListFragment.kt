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
import com.yashishu.bsa.databinding.FragmentProductListBinding
import com.yashishu.bsa.ui.vendor.AddProductFragment.Companion.COLL_PRODUCT

class ProductListFragment: Fragment() {
    private var _binding: FragmentProductListBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= Firebase.auth
        db= Firebase.firestore
        storage= Firebase.storage

    }
    fun getProducts(){
        db.collection(COLL_PRODUCT).get().addOnFailureListener {

        }.addOnSuccessListener {

        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentProductListBinding.inflate(inflater,container,false)
        return binding.root
    }
}