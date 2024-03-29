package com.yashishu.bsa.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by activityViewModels()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnCustSupport.setOnClickListener { gotoSupportScreen() }
            btnChatbot.setOnClickListener { gotoChatBotScreen() }
            btnOrders.setOnClickListener { gotoOrderScreen() }
            //btnTrackOrders.setOnClickListener { gotoTrackOrderScreen() }
            //btnProfile.setOnClickListener {         findNavController().navigate(R.id.action_user_nav_dashboard_to_my_profile)}

            }
        }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun gotoSupportScreen() {
        findNavController().navigate(R.id.action_user_nav_dashboard_to_customerSupport)
    }

    private fun gotoChatBotScreen() {
        findNavController().navigate(R.id.action_user_nav_dashboard_to_chatBotFragment)
    }

    private fun gotoOrderScreen() {
        findNavController().navigate(R.id.action_user_nav_dashboard_to_user_nav_order)
    }

}