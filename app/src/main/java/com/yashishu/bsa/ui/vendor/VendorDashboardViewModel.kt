package com.yashishu.bsa.ui.vendor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.models.Product

class VendorDashboardViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun getProducts(db: FirebaseFirestore, vendorId: String) {
        loadProducts(db, vendorId)
    }

    private fun loadProducts(db: FirebaseFirestore, vendorId: String) {
        db.collection(COL_PRODUCTS).whereEqualTo("vid", vendorId).get().addOnFailureListener {
        }.addOnCanceledListener {
            Log.e("VendorDashboardViewModel","Cancelled Fetching Products")
        }.addOnSuccessListener {
            val prds = it.toObjects(Product::class.java)
            _products.value = prds
            Log.d("VendorDashboardViewModel", "Products loaded ${prds.size}")
        }
    }

    companion object {
        const val COL_PRODUCTS = "products"
    }
}