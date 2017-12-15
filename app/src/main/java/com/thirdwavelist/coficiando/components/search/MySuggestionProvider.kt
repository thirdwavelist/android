package com.thirdwavelist.coficiando.components.search

import android.content.SearchRecentSuggestionsProvider


class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        val AUTHORITY = "com.example.MySuggestionProvider"
        val MODE = DATABASE_MODE_QUERIES
    }
}