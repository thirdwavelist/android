package com.thirdwavelist.coficiando.settings.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.thirdwavelist.coficiando.settings.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsFragmentViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }

        viewModel.onPrivacyPolicyTapped.observe(viewLifecycleOwner) {
            navigateToUrl(it)
        }

        viewModel.onFeedbackTapped.observe(viewLifecycleOwner) {
            navigateToUrl(it)
        }

        findPreference<Preference>(requireContext().getString(R.string.key_privacy_policy))?.setOnPreferenceClickListener { _ ->
            viewModel.privacyPolicyTapped()
            true
        }

        findPreference<Preference>(requireContext().getString(R.string.key_feedback))?.setOnPreferenceClickListener { _ ->
            viewModel.feedbackTapped()
            true
        }
    }

    private fun navigateToUrl(it: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = Uri.parse(it)
        }
        ContextCompat.startActivity(requireContext(), intent, null)
    }
}