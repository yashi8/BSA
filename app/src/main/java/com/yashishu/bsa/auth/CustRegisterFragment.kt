package com.yashishu.bsa.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentCustRegisterBinding

class CustRegisterFragment : Fragment() {

    private var _binding: FragmentCustRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var viewModel: CustRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustRegisterBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        db = Firebase.firestore
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            button.setOnClickListener { registerCustomer() }
        }
    }

    private fun registerCustomer() {
        binding.apply {
            val email = editemail.text.toString()
            val password = editpassword.text.toString()
            val name= editname.text.toString()
            val phone= editcontact.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && password.length >= 8 && name.isNotEmpty() && phone.length == 10 ) {
                button.isEnabled = false
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        if (it.user != null) {
                            val profileUpdates = userProfileChangeRequest {
                                displayName = binding.editname.text.toString()
                            }
                            it.user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    db.collection("accounts").document(it.user!!.uid).set(
                                        Customer(
                                            uid = it.user!!.uid,
                                            name = binding.editname.text.toString(),
                                            email = binding.editemail.text.toString(),
                                            phone = binding.editcontact.text.toString()
                                        )
                                    ).addOnSuccessListener { void ->
                                        updateUI(it.user)
                                    }.addOnFailureListener {
                                        showDialog(it.message)
                                    }
                                } else {
                                    showDialog(task.exception?.message)
                                    updateUI(null)
                                }
                            }
                            updateUI(it.user!!)
                        } else {
                            updateUI(null)
                        }
                    }
                    .addOnFailureListener {
                        showDialog(it.message)
                    }
            }
        }
    }

    private fun showDialog(message: String?) {
        // todo add a dialog box to show this message
        binding.button.isEnabled = true
    }

    private fun updateUI(user: FirebaseUser?) {

        if (user?.isEmailVerified == true) {
            navigateToHome()
        } else {
            user?.sendEmailVerification()
                ?.addOnFailureListener {
                    showDialog(it.message)
                }?.addOnSuccessListener {
                    showDialog("Please check your email ${user.email} for verification.")
                }
        }
    }

    private fun navigateToHome() {
        Toast.makeText(requireContext(), "no home", Toast.LENGTH_LONG).show()
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_custLoginFragment_to_custRegisterFragment)
    }

}