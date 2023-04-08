package com.yashishu.bsa.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yashishu.bsa.models.CartItem
import com.yashishu.bsa.models.Product
import org.json.JSONObject

class ProductViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> = _selectedProduct

    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems


    private val _isProductInCart = MutableLiveData<Boolean>()
    val isProductInCart: LiveData<Boolean> = _isProductInCart

    private var _cart: MutableLiveData<JSONObject> = MutableLiveData()
    val cart: MutableLiveData<JSONObject> = _cart


    fun setCart(amt: Float) {
        val cart = JSONObject()
        cart.put("currencyCode", "INR")
        cart.put("totalPrice", amt)
        cart.put("totalPriceStatus", "FINAL")
        cart.put("lineItems", listOf<JSONObject>())
        _cart.value = cart
    }


    fun setQuery(query: String) {
        _query.value = query
    }

    fun setProduct(product: Product) {
        _selectedProduct.value = product
    }

    private fun loadProducts(db: FirebaseFirestore) {
        db.collection(COLL_PRODUCTS).get().addOnFailureListener {}.addOnCanceledListener {
            Log.e("UserViewModel", "Cancelled Fetching Products")
        }.addOnSuccessListener {
            val prds = it.toObjects(Product::class.java)
            _products.value = prds
            Log.d("UserViewModel", "Products loaded ${prds.size}")
        }
    }

    private fun getRandom6Products(db: FirebaseFirestore) {
        db.collection(COLL_PRODUCTS).get().addOnFailureListener {}.addOnCanceledListener {
            Log.e("UserViewModel", "Cancelled Fetching Products")
        }.addOnSuccessListener {
            val prds = it.toObjects(Product::class.java)
            prds.shuffle()
            _products.value = prds.subList(0, 6)
            Log.d("UserViewModel", "Random Products loaded ${prds.size}")
        }
    }

    private fun loadProductsByQuery(db: FirebaseFirestore, query: String) {
        db.collection(COLL_PRODUCTS).whereLessThanOrEqualTo("title", query)
            .whereGreaterThanOrEqualTo("title", query + "\uf8ff").get().addOnFailureListener {}
            .addOnCanceledListener {
                Log.e("UserViewModel", "Cancelled Fetching Products")
            }.addOnSuccessListener {
                val prds = it.toObjects(Product::class.java)
                _products.value = prds
                Log.d("UserViewModel", "Products loaded ${prds.size}")
            }
    }

    private fun loadProductsByCategory(db: FirebaseFirestore, category: String) {
        db.collection(COLL_PRODUCTS).whereEqualTo("category", category).get().addOnFailureListener {}
            .addOnCanceledListener {
                Log.e("UserViewModel", "Cancelled Fetching Products")
            }.addOnSuccessListener {
                val prds = it.toObjects(Product::class.java)
                _products.value = prds
                Log.d("UserViewModel", "Products loaded ${prds.size}")
            }
    }

    fun getProducts(db: FirebaseFirestore) {
        loadProducts(db)
    }

    fun getRecentProducts(db: FirebaseFirestore) {
        getRandom6Products(db)
    }

    fun getProductByCategory(db: FirebaseFirestore, category: String) {
        loadProductsByCategory(db, category)
    }

    fun getProductByQuery(db: FirebaseFirestore, query: String) {
        loadProductsByQuery(db, query)
    }

    fun getCartItems(db: FirebaseFirestore, auth: FirebaseAuth) {
        loadCartItems(db, auth.currentUser!!.uid)
    }

    private fun loadCartItems(db: FirebaseFirestore, uid: String) {
        db.collection(COL_CART).whereEqualTo("uid", uid).get().addOnFailureListener {}
            .addOnCanceledListener {
                Log.e("UserViewModel", "Cancelled Fetching Products")
            }.addOnSuccessListener {
                val cartItems = it.toObjects(CartItem::class.java)
                _cartItems.value = cartItems
                Log.d("UserViewModel", "Cart Items loaded ${cartItems.size}")
            }
    }


    fun getProductByVendor(db: FirebaseFirestore, vid: String) {
        loadProductsByVendor(db, vid)
    }

    private fun loadProductsByVendor(db: FirebaseFirestore, vid: String) {
        db.collection(COLL_PRODUCTS).whereEqualTo("vid", vid).get().addOnFailureListener {}
            .addOnCanceledListener {
                Log.e("UserViewModel", "Cancelled Fetching Products")
            }.addOnSuccessListener {
                val prds = it.toObjects(Product::class.java)
                _products.value = prds
                Log.d("UserViewModel", "Products loaded ${prds.size}")
            }
    }

    fun addToCart(db: FirebaseFirestore, qty: Int = 1) {
        val product = _selectedProduct.value!!
        val cartItem = CartItem(
            product,
            FirebaseAuth.getInstance().currentUser!!.uid,
            price = product.price.toInt(),
            qty = qty
        )
        // store object in localcart

        // store object in firestore
        db.collection(COL_CART).add(cartItem).addOnSuccessListener {
            Log.d("UserViewModel", "Added to cart")
            _isProductInCart.value = true
        }.addOnFailureListener {
            Log.e("UserViewModel", "Failed to add to cart")
        }
    }

    fun handleProductInCart(db: FirebaseFirestore, auth: FirebaseAuth) {
        // if product is local cart then remove it from local cart and and cart
        if (isProductInCart.value == true) {
            val product = _selectedProduct.value!!
            val cartItem = CartItem(product, FirebaseAuth.getInstance().currentUser!!.uid)
            removeFromCart(db, auth, cartItem)
        } else {
            addToCart(db)
        }
    }


    private fun removeFromCart(db: FirebaseFirestore, auth: FirebaseAuth, cartItem: CartItem) {

        val product = cartItem.product
        db.collection(COL_CART).whereEqualTo("uid", auth.currentUser!!.uid)
            .whereEqualTo("product", product).get().addOnSuccessListener {
                it.documents.forEach { doc ->
                    db.collection(COL_CART).document(doc.id).delete().addOnSuccessListener {
                        Log.d("UserViewModel", "Removed from cart")
                        _isProductInCart.value = false
                    }.addOnFailureListener {
                        Log.e("UserViewModel", "Failed to remove from cart")
                    }
                }
            }
    }

    fun removeCartItem(db: FirebaseFirestore, auth: FirebaseAuth, item: CartItem?) {
        removeCartItem(db, auth, item)
    }

    fun isProductInCloudCart(db: FirebaseFirestore, auth: FirebaseAuth) {
        val product = _selectedProduct.value!!
        val cartItem = CartItem(product, auth.currentUser!!.uid)
        db.collection(COL_CART).whereEqualTo("uid", auth.currentUser!!.uid)
            .whereEqualTo("product", product).get().addOnSuccessListener {
                _isProductInCart.value = it.documents.isNotEmpty()
            }
    }


    companion object {
        const val COLL_PRODUCTS = "products"
        const val COL_CART = "cart"
    }
}