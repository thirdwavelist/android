package com.thirdwavelist.coficiando.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.thirdwavelist.coficiando.core.data.repository.cafe.CafeRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: DetailsFragmentBinding
    private val viewModel: DetailsFragmentViewModel by lazy { DetailsFragmentViewModel(cafeRepository) }
    private val args: DetailsFragmentArgs by navArgs()
    @Inject
    lateinit var cafeRepository: CafeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        viewModel.loadCafe(UUID.fromString(args.id))
    }
}