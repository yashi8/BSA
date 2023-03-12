package com.yashishu.bsa.ui.vendor

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import com.yashishu.bsa.models.Product
import android.Manifest.permission.*

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private var isImgUploaded = false
    private var isImgSelected = false
    private var downloadUri: Uri? = null


    companion object {
        const val REQUEST_IMAGE_GET = 12
        const val COLL_PRODUCT = "products"
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
        val adapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.products,
            android.R.layout.simple_list_item_1
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editSpinner.adapter = adapter
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

    private fun saveToFireStore(
        title: String,
        desc: String,
        selectedCategory: String,
        price: String
    ) {
        db.collection(COLL_PRODUCT).add(
            Product(
                title,
                desc,
                price,
                downloadUri.toString(),
                auth.currentUser!!.uid,
                selectedCategory
            )
        ).addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
            findNavController().navigate(R.id.action_add_product_fragment_to_navigation_vendor_product)
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun addVendorProduct() {
        binding.apply {
            val title = proTitle.text.toString().trim()
            val desc = proDesc.text.toString().trim()
            val selectedCategory = binding.editSpinner.selectedItem.toString()

            val price = proCost.text.toString().trim()
            if (title.isNotEmpty() && desc.isNotEmpty() && price.isNotEmpty() && downloadUri != null && selectedCategory.isNotEmpty()) {

                btnAdd.isEnabled = false
                if (isImgUploaded) {
                    saveToFireStore(title, desc, selectedCategory, price)
                }
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