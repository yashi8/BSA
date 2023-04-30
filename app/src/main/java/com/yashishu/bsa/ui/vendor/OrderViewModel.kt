package com.yashishu.bsa.ui.vendor

import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yashishu.bsa.models.Order
import com.yashishu.bsa.models.Product
import kotlinx.coroutines.launch

enum class OrderStatus { PENDING, ACCEPTED, REJECTED, COMPLETED }
enum class LoadingStatus { LOADING, ERROR, DONE, NONE }
class OrderViewModel(
    private val db: FirebaseFirestore, private val auth: FirebaseAuth
) : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>(emptyList())
    val orders: LiveData<List<Order>> = _orders

    private val _status = MutableLiveData<LoadingStatus>(LoadingStatus.NONE)
    val status: LiveData<LoadingStatus> = _status

    private val _order = MutableLiveData<Order?>()
    val order: LiveData<Order?> = _order

    private val _orderedProducts = MutableLiveData<List<Product>>(emptyList())
    val orderedProducts: LiveData<List<Product>> = _orderedProducts


    fun getVendorOrders() {
        _status.value = LoadingStatus.LOADING
        db.collection("orders").get().addOnFailureListener {
            _status.value = LoadingStatus.ERROR
        }.addOnSuccessListener { result ->
            val allOrders = result.toObjects(Order::class.java)
            val vendorOrders = arrayListOf<Order>()
            allOrders.forEach { order ->
                for (item in order.items) {
                    if (item.product.vid == auth.currentUser?.uid) {
                        order.orderId = result.documents[allOrders.indexOf(order)].id
                        vendorOrders.add(order)
                        break
                    }
                }
            }
            _orders.value = vendorOrders
            _status.value = LoadingStatus.DONE
            Log.d("Orders", "Orders: $vendorOrders")
        }
    }

    fun getOrderProducts(orderId: String) {
        _status.value = LoadingStatus.LOADING
        db.collection("orders").document(orderId).get().addOnFailureListener {
            _status.value = LoadingStatus.ERROR
        }.addOnSuccessListener { result ->
            val order = result.toObject(Order::class.java)
            order?.orderId = result.id
            _order.value = order
            setOrderedProduct(order)
            _status.value = LoadingStatus.DONE
            Log.d("Order", "Order: $order")
        }
    }

    private fun setOrderedProduct(order: Order?) {
        val orderedProducts = arrayListOf<Product>()
        order?.items?.forEach { item ->
            if (item.product.vid == auth.currentUser?.uid) orderedProducts.add(item.product)
        }
        _orderedProducts.value = orderedProducts
    }

    fun updateOrderStatus(orderId: String, s: String) {
        _status.value = LoadingStatus.LOADING
        db.collection("orders").document(orderId).update("status", s).addOnFailureListener {
            _status.value = LoadingStatus.ERROR
        }.addOnSuccessListener {
            _status.value = LoadingStatus.DONE
        }
    }

    fun updateProductLocation(
        geocoder: Geocoder,
        orderId: String,
        product: Product,
        address: String
    ) {
        _status.value = LoadingStatus.LOADING
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocationName(address, 1) {
                    Log.d("OrderViewModel", "updateProductLocation: $it")
                    val lat = it[0].latitude
                    val lng = it[0].longitude
                    updateProductTracker(orderId, product, lat, lng, address)
                }
            } else {
                try {
                    val addresses = geocoder.getFromLocationName(address, 1)
                    val lat = addresses!![0].latitude
                    val lng = addresses!![0].longitude
                    updateProductTracker(orderId, product, lat, lng, address)
                } catch (e: Exception) {
                    updateProductTracker(orderId, product, 0.0, 0.0, address)
                }
            }

        }
    }

    private fun updateProductTracker(
        orderId: String,
        product: Product,
        lat: Double,
        lng: Double,
        address: String
    ) {
        db.collection("orders").document(orderId).get().addOnFailureListener {
            _status.value = LoadingStatus.ERROR
        }.addOnSuccessListener { result ->
            val order = result.toObject(Order::class.java)
            order?.items?.forEach { item ->
                if (item.product.vid == product.vid && item.product.vid == auth.currentUser?.uid) {
                    item.product.lat = lat
                    item.product.lng = lng
                    item.product.address = address
                }
            }
            db.collection("orders").document(orderId).set(order!!)
                .addOnFailureListener {
                    _status.value = LoadingStatus.ERROR
                }.addOnSuccessListener {
                    _status.value = LoadingStatus.DONE
                }
        }

    }

}