package com.yashishu.bsa.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aemerse.slider.model.CarouselItem
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentUserHomeBinding


class UserHomeFragment : Fragment() {
    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!

    val imageList = mutableListOf<CarouselItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                caption = " "
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
                caption = "Photo "
            )
        )

        binding.carousel.setData(imageList)


        binding.carousel1.registerLifecycle(lifecycle)
        imageList.add(
            CarouselItem(
                imageDrawable = R.drawable.plant1,
                caption = "Bring Peace and Greenery"
            )
        )

        imageList.add(
            CarouselItem(
                imageDrawable = R.drawable.plant3,
                caption = " "
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
                caption = " "
            )
        )
        binding.carousel1.setData(imageList)


    }







}