package com.thirdwavelist.coficiando.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thirdwavelist.coficiando.core.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.core.storage.repository.cafe.CafeRepository
import com.thirdwavelist.coficiando.navigation.NavigationFlow
import com.thirdwavelist.coficiando.navigation.NavigationOrchestrator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var onItemClickListener: CafeAdapter.OnItemSelectedListener = object : CafeAdapter.OnItemSelectedListener {
        override fun onItemSelected(item: CafeItem, sharedElementTransitions: Array<Pair<View, String>>) {
            (requireActivity() as NavigationOrchestrator).navigateToFlow(NavigationFlow.DetailsFlow(item.id.toString()), sharedElementTransitions)
        }
    }

    private val viewModel: HomeFragmentViewModel by lazy { HomeFragmentViewModel(cafeRepository, CafeAdapter(onItemClickListener)) }
    private lateinit var binding: HomeFragmentBinding
    @Inject
    lateinit var cafeRepository: CafeRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.recycler.layoutManager = getLayoutManager(requireContext())
        viewModel.loadCafes()
    }

    private fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        val smallestWidth = resources.configuration.smallestScreenWidthDp

        return when {
            smallestWidth >= 600 -> GridLayoutManager(context, 2)
            resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, 2)
        }
    }

    override fun onStop() {
        viewModel.dispose()
        super.onStop()
    }
}