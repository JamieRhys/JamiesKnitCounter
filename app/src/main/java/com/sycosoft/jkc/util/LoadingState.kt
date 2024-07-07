package com.sycosoft.jkc.util

/** The loading state of the app when communicating to the database. */
enum class LoadingState {
    Idle,
    Success,
    Loading,
    Failure,
}