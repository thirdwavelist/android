package com.thirdwavelist.coficiando.coreutils.ext

/**
 * Extension function for `when` cases to help exhaust the options. Due to the inner working of `when`
 * the branches are only checked if the result is read, therefore this extension helps with that.
 *
 * ```
 * when (state) {
 *     is Success -> { }
 *     is Failed -> { }
 * }
 *
 * sealed class ViewState
 * object Success: ViewState
 * object Failed: ViewState
 * ```
 */
inline val <T> T.exhaustive get() = this