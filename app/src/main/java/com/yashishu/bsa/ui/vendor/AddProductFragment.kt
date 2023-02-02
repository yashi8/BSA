package com.yashishu.bsa.ui.vendor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding?=null
    private val binding get()=_binding!!
    private lateinit var auth :FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private lateinit var viewModel: AddProductViewModel
    companion object {
        fun newInstance() = AddProductFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAddProductBinding.inflate((inflater,container,false)
        auth = Firebase.auth
        db = Firebase.firestore
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { btnAdd.setOnClickListener { addVendorProduct() } }

    }

    private fun addVendorProduct() {
        binding.apply {
            val name= proTitle.text.toString()
            val desc= proDesc.text.toString()
            val cost= proCost.text.toString() //doubt
            if(name.isNotEmpty()&& desc.isNotEmpty()&& cost.isNotEmpty()){
                btnAdd.isEnabled=false

            }

        }
    }
}