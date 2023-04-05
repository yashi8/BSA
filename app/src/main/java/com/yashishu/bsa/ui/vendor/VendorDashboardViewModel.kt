package com.yashishu.bsa.ui.vendor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yashishu.bsa.ui.vendor.AddProductFragment.Companion.COL_PRODUCT

import com.yashishu.bsa.models.Product

enum class ProductState { LOADING, SAVED, ERROR, NONE }
class VendorDashboardViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> = _selectedProduct

    private val _saveState = MutableLiveData<ProductState>(ProductState.NONE)
    val saveState: LiveData<ProductState> = _saveState

    fun getProducts(db: FirebaseFirestore, vendorId: String) {
        loadProducts(db, vendorId)
    }

    private fun loadProducts(db: FirebaseFirestore, vendorId: String) {
        // fetch data from firebase firestore

        db.collection(COL_PRODUCT).whereEqualTo("vid", vendorId).get().addOnFailureListener {
        }.addOnCanceledListener {
            Log.e("VendorDashboardViewModel","Cancelled Fetching Products")
        }.addOnSuccessListener {
            val prds = it.toObjects(Product::class.java)
            _products.value = prds
            Log.d("VendorDashboardViewModel", "Products loaded ${prds.size}")
        }
    }

    fun setProduct(product: Product) {
        _selectedProduct.value=product
    }

    fun deleteProduct(db: FirebaseFirestore) {
        db.collection(COL_PRODUCT).whereEqualTo("title", _selectedProduct.value?.title).get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    db.collection(COL_PRODUCT).document(it.documents[0].id).delete().addOnSuccessListener {
                        //doubt about vendor id
                        loadProducts(db, auth.currentUser!!.uid)
                    }
                }
            }
    }

    fun updateProduct(
        db: FirebaseFirestore,
        title: String,
        desc: String,
        price: String

    ) {
        // update product in firebase firestore
        _saveState.value = ProductState.LOADING
        db.collection(COL_PRODUCT).whereEqualTo("name", _selectedProduct.value?.title).get()
            .addOnSuccessListener { query ->
                if (query.isEmpty) {
                    _saveState.value = ProductState.ERROR
                } else {
                    val product = query.documents[0].toObject(Product::class.java)
                    product?.let {
                        it.title = title
                        it.desc = desc
                        it.price = price.toDouble().toString()
                        db.collection(COL_PRODUCT).document(query.documents[0].id).set(it)
                            .addOnSuccessListener {
                                _saveState.value = ProductState.SAVED
                            }.addOnFailureListener {
                                _saveState.value = ProductState.ERROR
                            }
                    }
                }
            }.addOnFailureListener {
                _saveState.value = ProductState.ERROR
            }
    }

    fun resetProductState() {
        _saveState.value = ProductState.NONE
    }
}

