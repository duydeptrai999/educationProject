package com.example.asm2f.ui.fragment

import ViewPagerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.asm2f.databinding.FragmentMovieBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment : Fragment(), ToggleAdapterFragment,OnFragmentInteractionListener {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.viewPager
        adapter = ViewPagerAdapter(requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()

        binding.imgSwitch.setOnClickListener {
            Log.d("MovieFragment", "imgSwitch clicked")
            toggleAdapter()
        }
    }



    override fun toggleAdapter() {
        if (::adapter.isInitialized) {
            val currentItem = binding.viewPager.currentItem
            val currentFragment = adapter.getFragment(currentItem)

            Log.d("MovieFragment", "Current item: $currentItem")
            Log.d("MovieFragment", "Current fragment: ${currentFragment?.javaClass?.name}")

            if (currentFragment is ToggleAdapterFragment) {
                currentFragment.toggleAdapter()
            } else {
                Log.d("MovieFragment", "Current fragment is not ToggleAdapterFragment")
            }
        } else {
            Log.d("MovieFragment", "Adapter is not initialized")
        }
    }
    override fun onPageRequested(pageIndex: Int) {
        viewPager.setCurrentItem(pageIndex, true)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface OnFragmentInteractionListener {
    fun onPageRequested(pageIndex: Int)
}