package com.yashishu.bsa.auth

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
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
import com.yashishu.bsa.MainActivity
import com.yashishu.bsa.PrefUtil
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentVndrRegisterBinding

class VndrRegisterFragment : Fragment() {
    private var _binding: FragmentVndrRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var viewModel: VndrRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVndrRegisterBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        db = Firebase.firestore
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnvndrRegister.setOnClickListener { registerVendor() }
        }
    }

    private fun registerVendor() {
        binding.apply {
            val email = veditemail1.text.toString()
            val password = veditpassword1.text.toString()
            val name = veditname.text.toString()
            val phone = veditcontact1.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && password.length >= 8 && name.isNotEmpty() && phone.length == 10) {
                btnvndrRegister.isEnabled = false
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        if (it.user != null) {
                            val profileUpdates = userProfileChangeRequest {
                                displayName = binding.veditname.text.toString()
                            }
                            it.user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    db.collection("accounts").document(it.user!!.uid).set(
                                        Vendor(
                                            uid = it.user!!.uid,
                                            name = binding.veditname.text.toString(),
                                            email = binding.veditemail1.text.toString(),
                                            phone = binding.veditcontact1.text.toString()
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

        AlertDialog.Builder(requireContext()).apply {
            setMessage(message)
            setTitle("Register")
            setPositiveButton("Ok") { dialog, _ -> }
            create()
            show()
        }

        binding.btnvndrRegister.isEnabled = true
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
        PrefUtil(requireActivity()).setUserType(1)
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_vndrLoginFragment_to_vndrRegisterFragment)
    }


}