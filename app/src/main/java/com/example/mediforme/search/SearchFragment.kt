package com.example.mediforme.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentSearchBinding
import com.example.mediforme.databinding.FragmentSearchWithBackgroundBinding

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchWithBackgroundBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchWithBackgroundBinding.inflate(inflater, container, false)

        binding.searchWithCamera.setOnClickListener {
            // Use requireContext() to get the context
            startActivity(Intent(requireContext(), SearchResultActivity::class.java))
        }

        return binding.root
    }
}
