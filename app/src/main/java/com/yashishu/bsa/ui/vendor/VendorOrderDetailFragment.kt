package com.yashishu.bsa.ui.vendor

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.R
import com.yashishu.bsa.adapter.OrderedProductAdapter
import com.yashishu.bsa.databinding.FragmentVendorOrderDetailBinding
import com.yashishu.bsa.models.Product

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
                showSetLocationDialog(it)
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

    private fun showSetLocationDialog(product: Product) {
        val view = layoutInflater.inflate(R.layout.dialog_update_location, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update") { _, _ ->
            val address =
                view.findViewById<TextInputEditText>(R.id.editLocationEditText).text.toString()
            if (address.isEmpty()) {
                return@setButton
            }
            val geocoder = Geocoder(requireContext())
            viewModel.updateProductLocation(geocoder, args.orderId, product, address)
        }
        dialog.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}