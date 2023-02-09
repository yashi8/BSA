package com.yashishu.bsa.ui.vendor

import android.Manifest.permission
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private var isImgUploaded = false
    private var isImgSelected = false
    private  var downloadUri: Uri? = null

    companion object {
        const val REQUEST_IMAGE_GET = 12
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnAdd.setOnClickListener { addVendorProduct() }
            if (!isImgSelected) {
                imgProduct.setOnClickListener { selectImage() }
            }
            imgProduct.setOnLongClickListener {
                selectImage()
                true
            }
        }

    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

            binding.imgProduct.load(uri) {
                crossfade(true)
                transformations(CircleCropTransformation())
                uploadToFirebase(uri)
            }
        }

    @SuppressLint("Range")
    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }

    private fun uploadToFirebase(imageUri: Uri?) {
        val storageRef = storage.reference
        imageUri?.let { uri ->
            val filename = context?.let { getFileNameFromUri(it, uri) }
            val fileRef = storageRef.child(filename.toString())
            val uploadTask = fileRef.putFile(imageUri)
            uploadTask.addOnFailureListener {
                isImgUploaded = false
                it.message?.let { it1 -> showSnackBar(binding.imgProduct, it1) }
            }.continueWithTask { task ->
                if (!task.isSuccessful) {
                    isImgUploaded = false
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task2 ->
                if (task2.isSuccessful) {
                    downloadUri = task2.result
                    isImgUploaded = true

                } else {
                    isImgUploaded = false
                }
            }
        }


    }

    private fun saveToFireStore(downloadUri: Uri?) {

    }

    private fun showSnackBar(view: View, msg: String) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun addVendorProduct() {
        binding.apply {
            val name = proTitle.text.toString()
            val desc = proDesc.text.toString()
            val cost = proCost.text.toString() //doubt
            if (name.isNotEmpty() && desc.isNotEmpty() && cost.isNotEmpty() && downloadUri != null) {
                btnAdd.isEnabled = false
            }
        }
    }

    @AfterPermissionGranted(REQUEST_IMAGE_GET)
    private fun selectImage() {
        if (EasyPermissions.hasPermissions(
                context,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        ) {
            getContent.launch("image/*")
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_required),
                REQUEST_IMAGE_GET, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
            )
        }
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