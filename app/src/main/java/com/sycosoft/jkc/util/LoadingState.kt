package com.sycosoft.jkc.util

/** This enum allows us to track the loading state of the page at any time. */
enum class LoadingState {
    Idle,
    Loading,
    Success,
    Failure,
}