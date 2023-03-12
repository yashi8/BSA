package com.yashishu.bsa.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.yashishu.bsa.models.Product

class ProductViewModel:ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> = _selectedProduct

    fun getProducts(db: FirebaseFirestore) {
        loadProducts(db)
    }

    private fun loadProducts(db: FirebaseFirestore) {
        // todo will sort later
        db.collection(COL_PRODUCTS).get().addOnFailureListener {
        }.addOnCanceledListener {
            Log.e("UserViewModel","Cancelled Fetching Products")
        }.addOnSuccessListener {
            val prds = it.toObjects(Product::class.java)
            _products.value = prds
            Log.d("UserViewModel", "Products loaded ${prds.size}")
        }
    }

    fun setProduct(product: Product) {
        _selectedProduct.value=product
    }


    companion object {
        const val COL_PRODUCTS = "products"
    }
}