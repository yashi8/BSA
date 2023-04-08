package com.yashishu.bsa.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.aemerse.slider.model.CarouselItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.R
import com.yashishu.bsa.adapter.ProductSliderAdapter
import com.yashishu.bsa.databinding.FragmentUserHomeBinding


class UserHomeFragment : Fragment() {
    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val imageList = mutableListOf<CarouselItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_home, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCarousel()
        viewModel.getRecentProducts(db)
        val adapter = ProductSliderAdapter {
            viewModel.setProduct(it)
            findNavController().navigate(R.id.action_user_nav_home_to_productDetailFragment)
        }
        binding.rvTopProduct.layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        binding.rvTopProduct.adapter = adapter
        binding.fabViewAll.setOnClickListener { findNavController().navigate(R.id.action_user_nav_home_to_user_nav_product) }
        viewModel.products.observe(viewLifecycleOwner) { adapter.submitList(it) }


    }

    private fun setupCarousel() {
        binding.carousel.registerLifecycle(lifecycle)

        imageList.add(
            CarouselItem(
                imageDrawable = R.drawable.plant1,
                caption = "Bring Peace and Greenery"
            )
        )

        imageList.add(
            CarouselItem(
                imageDrawable = R.drawable.plant3,
                caption = "Besides of Beauty plant for life"
            )
        )

        imageList.add(
            CarouselItem(
                imageDrawable = R.drawable.plant7,
                caption = "Open Box Delivery"
            )
        )



        imageList.add(
            CarouselItem(
                imageDrawable = R.drawable.msg4,
                caption = "shop now"
            )

        )

        imageList.add(
            CarouselItem(
                imageDrawable = R.drawable.cart,
                caption = "Lets Pay Back Our Planet"
            )
        )

        binding.carousel.setData(imageList)
    }


}