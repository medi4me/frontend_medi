package com.example.mediforme.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mediforme.R

class TabItemFragment : Fragment() {

    companion object {
        private const val ARG_TITLE = "title"

        fun newInstance(title: String): TabItemFragment {
            val fragment = TabItemFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab_item, container, false)
        val title = arguments?.getString(ARG_TITLE)
        view.findViewById<TextView>(R.id.ingredients_contents_tv).text = title
        return view
    }
}
