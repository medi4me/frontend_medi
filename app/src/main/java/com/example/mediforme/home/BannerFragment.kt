package com.example.mediforme.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mediforme.databinding.FragmentBannerBinding

class BannerFragment : Fragment() {

    private var imgRes: Int = 0
    private lateinit var binding: FragmentBannerBinding

    companion object {
        private const val ARG_IMG_RES = "img_res"

        // Factory method to create a new instance of the fragment with parameters
        fun newInstance(imgRes: Int): BannerFragment {
            val fragment = BannerFragment()
            val args = Bundle()
            args.putInt(ARG_IMG_RES, imgRes)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)
        imgRes = arguments?.getInt(ARG_IMG_RES) ?: 0
        binding.bannerImageIv.setImageResource(imgRes)
        return binding.root
    }
}
