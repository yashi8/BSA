package com.yashishu.bsa.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.yashishu.bsa.R
import com.yashishu.bsa.adapter.ProductAdapter
import com.yashishu.bsa.databinding.FragmentProductListBinding
import com.yashishu.bsa.ui.vendor.AddProductFragment.Companion.COLL_PRODUCT

class ProductListFragment : Fragment() {
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private val viewModel: VendorDashboardViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth.currentUser?.let { viewModel.getProducts(db, it.uid) } // fetch data only
        viewModel.products.observe(viewLifecycleOwner) { products ->
            if (products.isNotEmpty()) {
                binding.productRecyclerView.adapter = ProductAdapter(requireActivity())
                (binding.productRecyclerView.adapter as ProductAdapter).submitList(products)
            } else {
                binding.productRecyclerView.visibility = View.GONE
            }
            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            auth.currentUser?.uid?.let { viewModel.getProducts(db, it) }
        }

//        binding.fabAdd.setOnClickListener { findNavController().navigate(R.id.) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
        return binding.root
    }


}