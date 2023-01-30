package com.yashishu.bsa.ui.vendor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yashishu.bsa.R

class VendorDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = VendorDashboardFragment()
    }

    private lateinit var viewModel: VendorDashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vendor_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VendorDashboardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}