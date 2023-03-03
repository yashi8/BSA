package com.yashishu.bsa.ui.vendor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentVendorDashboardBinding
import com.yashishu.bsa.ui.vendor.VendorDashboardViewModel

class VendorDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = VendorDashboardFragment()
    }

    private val viewModel: VendorDashboardViewModel by activityViewModels()
    private var _binding: FragmentVendorDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_vendor_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            addpro.setOnClickListener { gotoAddProductScreen() }
            prolist.setOnClickListener { gotoProductListScreen() }
            btnCustmerSupport.setOnClickListener{ gotoSupportScreen()}
            vendorOrders.setOnClickListener{ gotoOrdersScreen()}
            viewModel = viewModel
        }
    }

    private fun gotoOrdersScreen() {
        findNavController().navigate(R.id.action_vendorDashboardFragment_to_ordersFragment)
    }

    private fun gotoSupportScreen() {
        findNavController().navigate(R.id.action_vendorDashboardFragment_to_customerSupport)
    }


    private fun gotoProductListScreen() {
        findNavController().navigate(R.id.action_vendorDashboardFragment_to_productListFragment)
    }

    private fun gotoAddProductScreen() {
        findNavController().navigate(R.id.action_vendorDashboardFragment_to_addProductFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}