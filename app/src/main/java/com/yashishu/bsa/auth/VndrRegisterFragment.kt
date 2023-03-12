package com.yashishu.bsa.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
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
import com.yashishu.bsa.auth.VndrRegisterFragment.Companion.COLL_ACCOUNT
import com.yashishu.bsa.databinding.FragmentVndrRegisterBinding
import java.security.Permission

class VndrRegisterFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private var hasPermission: Boolean = false
    private var _binding: FragmentVndrRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var viewModel: VndrRegisterViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null

    companion object {
        const val REQUEST_LOCATION_GET = 112
        const val COLL_ACCOUNT = "accounts"
    }

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (!hasPermission) {
            getPermission()
        } else {
            getLastLocation()
        }
        binding.apply {
            btnvndrRegister.setOnClickListener { registerVendor() }
        }
    }

    private fun getPermission() {
        Log.d("PERMISSION", "requesting location")
        EasyPermissions.requestPermissions(
            this,
            "give me the location",
            REQUEST_LOCATION_GET,
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
                                    db.collection(COLL_ACCOUNT).document(it.user!!.uid).set(
                                        if (location != null && hasPermission) {
                                            Vendor(
                                                uid = it.user!!.uid,
                                                name = binding.veditname.text.toString(),
                                                email = binding.veditemail1.text.toString(),
                                                phone = binding.veditcontact1.text.toString(),
                                                lat = location!!.latitude,
                                                lng = location!!.longitude
                                            )
                                        } else {
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

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

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