package com.yashishu.bsa.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vmadalin.easypermissions.EasyPermissions
import com.yashishu.bsa.MainActivity
import com.yashishu.bsa.PrefUtil
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentCustRegisterBinding

class CustRegisterFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentCustRegisterBinding? = null
    private val binding get() = _binding!!
    private var hasPermission: Boolean = false
    private var location: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (!hasPermission) {
            getPermission()
        } else {
            getLastLocation()
        }
        binding.apply {
            button.setOnClickListener { registerCustomer() }
        }
    }
    private fun getPermission() {
        Log.d("PERMISSION", "requesting location")
        EasyPermissions.requestPermissions(
            this,
            "give me the location",
            VndrRegisterFragment.REQUEST_LOCATION_GET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        Log.d("PERMISSION", "location permission done, fetch last location")
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                    Log.d("location","${location.latitude}, ${location.longitude}")
                } else {
                    Snackbar.make(binding.root, "location not available", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Snackbar.make(binding.root, "error ${it.message}", Snackbar.LENGTH_SHORT)
                    .show()

            }
    }

    private fun registerCustomer() {
        binding.apply {
            val email = editemail.text.toString()
            val password = editpassword.text.toString()
            val name = editname.text.toString()
            val phone = editcontact.text.toString()
            var error=false
            if (email.isEmpty()){
                binding.editemail.error="Please provide email"
                error= true
            }

            if (password.isEmpty()){
                binding.editpassword.error="Please provide password"
                error= true
            }

            if (name.isEmpty()){
                binding.editname.error="Please Enter Confirm Password"
                error=true
            }

            if (phone.isEmpty()){
                binding.editcontact.error="Please Enter Confirm Password"
                error=true
            }
            if (error) {
                return
            }
            if (email.isNotEmpty() && password.isNotEmpty() && password.length >= 8 && name.isNotEmpty() && phone.length == 10) {
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
                                        if (location != null && hasPermission) {
                                            Customer(
                                                uid = it.user!!.uid,
                                                name = binding.editname.text.toString(),
                                                email = binding.editemail.text.toString(),
                                                phone = binding.editcontact.text.toString(),
                                                lat = location!!.latitude,
                                                lng = location!!.longitude
                                            )
                                        }
                                            else{
                                                Snackbar.make(
                                                    binding.root,
                                                    "Could not register, location or permission not available",
                                                    Snackbar.LENGTH_SHORT
                                                )
                                                    .show()
                                            }

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
        AlertDialog.Builder(requireContext()).apply {
            setMessage(message)
            setTitle("Register")
            setPositiveButton("Ok") { dialog, _ -> }
            create()
            show()
        }
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
        PrefUtil(requireActivity()).setUserType(2)
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_custRegisterFragment_to_custLoginFragment)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
            hasPermission = true
            getLastLocation()
        }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}