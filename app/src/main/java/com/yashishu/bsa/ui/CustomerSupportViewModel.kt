package com.yashishu.bsa.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.yashishu.bsa.models.Product
import com.yashishu.bsa.models.SupportForm
import com.yashishu.bsa.ui.CustomerSupport.Companion.COLL_SUPPORT
import com.yashishu.bsa.ui.vendor.AddProductFragment

class CustomerSupportViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _isSaved = MutableLiveData(false)
    val isSaved: MutableLiveData<Boolean> = _isSaved

    private val _support=MutableLiveData<List<SupportForm>>()
    val support: LiveData<List<SupportForm>> = _support

    fun
            getForm(db: FirebaseFirestore) {
        loadProducts(db)
    }

    private fun loadProducts(db: FirebaseFirestore) {
        // fetch data from firebase firestore

        db.collection(CustomerSupport.COLL_SUPPORT).get().addOnFailureListener {
        }.addOnCanceledListener {
            Log.e("CustomerSupportViewModel","Cancelled Fetching Rwquests")
        }.addOnSuccessListener {
            val req = it.toObjects(SupportForm::class.java)
            _support.value = req
            Log.d("CustomerSupportViewModel", "Products loaded ${req.size}")
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun saveToFireStore(
        db: FirebaseFirestore,
        name: String,
        email: String,
        mobileno: String,
        issue: String
    ) {
        _isLoading.value = true

        if (!validateProduct(name, email,mobileno,issue)) {
            _isLoading.value = false
            _isSaved.value = false
        } else {
            val support = SupportForm(name, email,mobileno,issue)
               db.collection(COLL_SUPPORT).add(support).addOnSuccessListener {
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

    private fun validateProduct(name: String, email: String, mobileno: String,
                                issue: String): Boolean {
            return name.isNotEmpty() && email.isNotEmpty() && mobileno.isNotEmpty() && issue.isNotEmpty()
        }



    fun viewRequests(){

    }

}
