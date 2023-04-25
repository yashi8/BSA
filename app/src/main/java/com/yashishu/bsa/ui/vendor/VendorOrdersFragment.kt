package com.yashishu.bsa.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.adapter.VendorOrderAdapter
import com.yashishu.bsa.databinding.FragmentOrdersBinding

class VendorOrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by lazy {
        val db = Firebase.firestore
        val auth = Firebase.auth
        OrderViewModel(db, auth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getVendorOrders()
        viewModel.orders.observe(viewLifecycleOwner) {
            val adapter = VendorOrderAdapter {
                val dirs =
                    VendorOrdersFragmentDirections.actionVendorNavOrdersToVendorOrderDetailFragment(
                        it.orderId
                    )
                findNavController().navigate(dirs)
            }
            binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
            binding.rvOrders.adapter = adapter
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}