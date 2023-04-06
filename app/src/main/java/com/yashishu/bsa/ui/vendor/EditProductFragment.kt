package com.yashishu.bsa.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentEditProductBinding


class EditProductFragment : BottomSheetDialogFragment() {


    private val viewModel: VendorDashboardViewModel by activityViewModels()
    private  lateinit var db:FirebaseFirestore
    private lateinit var _binding:FragmentEditProductBinding
    private val binding get()= _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_product,container,false)
        binding.vm =viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.products,
            android.R.layout.simple_list_item_1
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editSpinner.adapter = adapter
        binding.btnEdit.setOnClickListener {
            val title = binding.proTitle2.text.toString()
            val category = binding.editSpinner.selectedItem.toString()
            val desc = binding.proDesc2.text.toString()
            val price = binding.proPrice2.text.toString()

            // valid if values are not empty
            if (title.isNotEmpty() && desc.isNotEmpty() && price.isNotEmpty() && category.isNotEmpty()) {
                viewModel.updateProduct(db, title, desc,category, price)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.saveState.observe(viewLifecycleOwner) {
            when (it) {
                ProductState.SAVED -> {
                    Toast.makeText(requireContext(), "Product saved", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editProductFragment_to_vendor_nav_product)
                }
                ProductState.ERROR -> {
                    Toast.makeText(requireContext(), "Error saving product", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

}