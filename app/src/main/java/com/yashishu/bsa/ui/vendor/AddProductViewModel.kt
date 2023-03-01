package com.yashishu.bsa.ui.vendor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.yashishu.bsa.models.Product
import com.yashishu.bsa.ui.vendor.VendorDashboardViewModel.Companion.COL_PRODUCTS

class AddProductViewModel : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _isSaved = MutableLiveData(false)
    val isSaved: MutableLiveData<Boolean> = _isSaved

    fun saveProduct(
        db: FirebaseFirestore,
        title: String,
        desc: String,
        selectedCategory: String,
        price: String
    ){

        _isLoading.value = true

        if (!validateProduct(title, desc, selectedCategory, price)) {
            _isLoading.value = false
            _isSaved.value = false
        } else {
            val product = Product(title,desc,selectedCategory,price)
            db.collection(COL_PRODUCTS).add(product).addOnSuccessListener {
                _isLoading.value = false
                _isSaved.value = true
            }.addOnFailureListener {
                _isLoading.value = false
                _isSaved.value = false
            }.addOnCanceledListener {
                _isLoading.value = false
                _isSaved.value = false
            }
        }
    }

    private fun validateProduct(
        title: String,
        desc: String,
        selectedCategory: String,
        price: String
    ): Boolean{
        return title.isNotEmpty() && desc.isNotEmpty() && selectedCategory.isNotEmpty() && price.isNotEmpty()
    }


}