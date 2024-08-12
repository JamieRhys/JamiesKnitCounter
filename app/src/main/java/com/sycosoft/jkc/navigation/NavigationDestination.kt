package com.sycosoft.jkc.navigation

sealed class NavigationDestination(val route: String) {
    data object AddCounterPage : NavigationDestination("add_counter_page")
    data object AddProjectPage : NavigationDestination("add_project_page")
    data object HomePage : NavigationDestination("home_page")
    data object ProjectPage : NavigationDestination("project_page")
}