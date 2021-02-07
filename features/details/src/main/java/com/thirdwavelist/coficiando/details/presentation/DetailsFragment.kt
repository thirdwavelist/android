package com.thirdwavelist.coficiando.details.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.thirdwavelist.coficiando.core.glide.setHtmlText
import com.thirdwavelist.coficiando.core.glide.setImage
import com.thirdwavelist.coficiando.core.glide.setIsVisibleWithAnimation
import com.thirdwavelist.coficiando.core.livedata.LiveEvent
import com.thirdwavelist.coficiando.coreutils.ext.exhaustive
import com.thirdwavelist.coficiando.details.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsFragmentViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view)

        viewModel.viewState.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.viewEvents.observe(viewLifecycleOwner) {
            handle(it)
        }

        viewModel.loadCafe(UUID.fromString(args.id))
    }

    private fun handle(liveEvent: LiveEvent<DetailsEvents>) {
        liveEvent.getContentIfNotHandled()?.let { event ->
            when (event) {
                is DetailsEvents.NavigateToUrl -> openExternalLink(event.url)
                is DetailsEvents.ReloadData -> {
                    viewModel.loadCafe(UUID.fromString(args.id))
                }
            }.exhaustive
        }
    }

    private fun render(viewState: DetailsViewState) {
        when (viewState) {
            is DetailsViewState.Loading -> {
                // Hide content/error
                setIsVisibleWithAnimation(binding.errorLayout, false, 350L)
                setIsVisibleWithAnimation(binding.contentLayout, false, 350L)

                // Show loading
                setIsVisibleWithAnimation(binding.loadingLayout, true, 350L)
            }
            is DetailsViewState.Error -> {
                // Hide content/loading
                setIsVisibleWithAnimation(binding.loadingLayout, false, 350L)
                setIsVisibleWithAnimation(binding.contentLayout, false, 350L)

                // Show error
                setIsVisibleWithAnimation(binding.errorLayout, true, 350L)

                binding.errorRetryButton.setOnClickListener {
                    viewModel.onRetryTapped()
                }
            }
            is DetailsViewState.Success -> {
                // Hide error/loading
                setIsVisibleWithAnimation(binding.loadingLayout, false, 350L)
                setIsVisibleWithAnimation(binding.errorLayout, false, 350L)

                // Header
                if (viewState.thumbnailUrl.startsWith("https://")) {
                    setImage(binding.thumbnail, Uri.parse(viewState.thumbnailUrl))
                }
                binding.toolbar.title = viewState.name

                // Fab
                binding.fab.setOnClickListener {
                    viewModel.onLinkButtonTapped(withUrl = viewState.googleMapsUri)
                }

                // Body
                viewState.grinderMachineName?.let { binding.grinderText.text = it }
                viewState.espressoMachineName?.let { binding.espressoText.text = it }
                viewState.availableBeanOrigin?.let { setHtmlText(binding.originText, it) }
                viewState.availableBeanRoast?.let { setHtmlText(binding.roastTypeText, it) }
                viewState.hasEspresso.let { binding.hasEspresso.isVisible = it }
                viewState.hasPourOver.let { binding.hasPourOver.isVisible = it }
                viewState.hasColdBrew.let { binding.hasColdBrew.isVisible = it }
                viewState.hasImmersive.let { binding.hasImmersive.isVisible = it }
                viewState.hasAeropress.let { binding.hasAeropress.isVisible = it }
                viewState.hasSyphon.let { binding.hasSyphon.isVisible = it }
                if (!viewState.facebookUri.isNullOrBlank()) {
                    binding.facebookLink.isVisible = true
                    binding.facebookLink.setOnClickListener {
                        viewModel.onLinkButtonTapped(withUrl = viewState.facebookUri)
                    }
                } else {
                    binding.facebookLink.isVisible = false
                }
                if (!viewState.instagramUri.isNullOrBlank()) {
                    binding.instagramLink.isVisible = true
                    binding.instagramLink.setOnClickListener {
                        viewModel.onLinkButtonTapped(withUrl = viewState.instagramUri)
                    }
                } else {
                    binding.instagramLink.isVisible = false
                }
                if (!viewState.websiteUri.isNullOrBlank()) {
                    binding.websiteLink.isVisible = true
                    binding.websiteLink.setOnClickListener {
                        viewModel.onLinkButtonTapped(withUrl = viewState.websiteUri)
                    }
                } else {
                    binding.websiteLink.isVisible = false
                }

                // Set layout to visible
                setIsVisibleWithAnimation(binding.contentLayout, true, 350L)
            }
        }.exhaustive
    }

    private fun openExternalLink(url: String) {
        view?.context?.let { context ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                addCategory(Intent.CATEGORY_BROWSABLE)
                data = Uri.parse(url)
            }
            ContextCompat.startActivity(context, intent, null)
        }
    }
}