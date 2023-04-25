package com.yashishu.bsa.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.adapter.OrderedProductAdapter
import com.yashishu.bsa.databinding.FragmentVendorOrderDetailBinding

class VendorOrderDetailFragment : Fragment() {
    private var _binding: FragmentVendorOrderDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by lazy {
        val db = Firebase.firestore
        val auth = Firebase.auth
        OrderViewModel(db, auth)
    }
    private val args: VendorOrderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVendorOrderDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getOrderProducts(args.orderId)
        viewModel.orderedProducts.observe(viewLifecycleOwner) {
            binding.rvOrderDetail.layoutManager = LinearLayoutManager(requireContext())
            val adapter = OrderedProductAdapter {
            }
            binding.rvOrderDetail.adapter = adapter
            adapter.submitList(it)
            if (viewModel.order.value?.status == "confirmed") {
                binding.button2.visibility = View.VISIBLE
            } else {
                binding.button2.visibility = View.GONE
            }
        }
        binding.button2.setOnClickListener {
            viewModel.updateOrderStatus(args.orderId, "Shipped")
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}