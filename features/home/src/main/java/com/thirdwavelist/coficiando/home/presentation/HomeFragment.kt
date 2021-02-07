package com.thirdwavelist.coficiando.home.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.core.glide.setIsVisibleWithAnimation
import com.thirdwavelist.coficiando.core.livedata.LiveEvent
import com.thirdwavelist.coficiando.coreutils.ext.exhaustive
import com.thirdwavelist.coficiando.home.databinding.FragmentHomeBinding
import com.thirdwavelist.coficiando.navigation.NavigationFlow
import com.thirdwavelist.coficiando.navigation.NavigationOrchestrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var cafeAdapter: CafeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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
                viewModel.onCafeTapped(cafeItem, sharedElementTransitions)
            }
        }
        binding.contentView.apply {
            adapter = cafeAdapter
            layoutManager = getLayoutManager(requireContext())
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.viewEvents.observe(viewLifecycleOwner) {
            handle(it)
        }

        viewModel.loadCafes()
    }

    private fun handle(liveEvent: LiveEvent<HomeEvents>) {
        liveEvent.getContentIfNotHandled()?.let { event ->
            when (event) {
                is HomeEvents.NavigateToDetails -> {
                    navigateToDetails(event.cafeId, event.sharedElementTransitions.toTypedArray())
                }
                is HomeEvents.ReloadData -> {
                    viewModel.loadCafes()
                }
            }.exhaustive
        }
    }

    private fun render(viewState: HomeViewState) {
        when (viewState) {
            is HomeViewState.Loading -> {
                // Hide content/error
                setIsVisibleWithAnimation(binding.errorLayout, false, 350L)
                setIsVisibleWithAnimation(binding.contentView, false, 350L)

                // Show loading
                setIsVisibleWithAnimation(binding.loadingLayout, true, 350L)
            }
            is HomeViewState.Error -> {
                // Hide content/loading
                setIsVisibleWithAnimation(binding.loadingLayout, false, 350L)
                setIsVisibleWithAnimation(binding.contentView, false, 350L)

                // Show error
                setIsVisibleWithAnimation(binding.errorLayout, true, 350L)

                binding.errorRetryButton.setOnClickListener {
                    viewModel.onRetryTapped()
                }
            }
            is HomeViewState.Success -> {
                // Hide error/loading
                setIsVisibleWithAnimation(binding.loadingLayout, false, 350L)
                setIsVisibleWithAnimation(binding.errorLayout, false, 350L)

                // Set contents
                cafeAdapter.submitList(viewState.cafes)

                // Set layout to visible
                setIsVisibleWithAnimation(binding.contentView, true, 350L)
            }
        }.exhaustive
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