package com.yashishu.bsa.ui


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.yashishu.bsa.databinding.FragmentCustomerSupportBinding
import com.yashishu.bsa.models.SupportForm
import com.yashishu.bsa.ui.vendor.AddProductFragment.Companion.COLL_PRODUCT

class CustomerSupport() :Fragment() {
    private var _binding: FragmentCustomerSupportBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage


    companion object {
        const val COLL_PRODUCT = "supportform"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerSupportBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnsubmit.setOnClickListener { submitForm() }
        }
    }

    private fun submitForm() {
        binding.apply {
            val name = personname.text.toString().trim()
            val email = editEmailAddress.text.toString().trim()
            val desc = editTextIssue.text.toString().trim()
            val mobileno = editTextPhone.text.toString().trim()

            if (name.isNotEmpty() && desc.isNotEmpty() && email.isNotEmpty() && mobileno.isNotEmpty()) {
                btnsubmit.isEnabled = false
                saveToFireStore(name, email, mobileno, desc)
            }
        }
    }


    private fun saveToFireStore(

        name: String,
        email: String,
        mobileno: String,
        desc: String
    ) {
        db.collection(COLL_PRODUCT).add(
            SupportForm(
                name,
                email,
                mobileno,
                desc,
                auth.currentUser!!.uid,
            )
        ).addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
            findNavController().navigateUp()
        }
    }

}



