package com.thirdwavelist.coficiando.home.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.home.HomeFragmentBinding
import com.thirdwavelist.coficiando.home.R
import com.thirdwavelist.coficiando.navigation.NavigationFlow
import com.thirdwavelist.coficiando.navigation.NavigationOrchestrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var binding: HomeFragmentBinding
    private lateinit var cafeAdapter: CafeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            v.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
        cafeAdapter = CafeAdapter().apply {
            setOnItemClickListener { cafeItem, sharedElementTransitions ->
                navigateToDetails(cafeItem, sharedElementTransitions)
            }
        }
        binding.recycler.apply {
            adapter = cafeAdapter
            layoutManager = getLayoutManager(requireContext())
        }

        viewModel.loadCafes()

        viewModel.cafes.observe(viewLifecycleOwner) {
            cafeAdapter.submitList(it)
        }
    }

    private fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        val smallestWidth = resources.configuration.smallestScreenWidthDp

        return when {
            smallestWidth >= 600 -> GridLayoutManager(context, 2)
            resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, 2)
        }
    }

    private fun navigateToDetails(item: CafeItem, sharedElementTransitions: Array<Pair<View, String>>) {
        (requireActivity() as NavigationOrchestrator).navigateToFlow(NavigationFlow.DetailsFlow(item.id.toString()), sharedElementTransitions)
    }
}