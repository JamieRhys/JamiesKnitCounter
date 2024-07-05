package com.sycosoft.jkc.navigation

sealed class NavigationDestination(val route: String) {
    data object HomePage : NavigationDestination("home")
    data object ProjectPage : NavigationDestination("project")
    data object AddProjectPage : NavigationDestination("add_project")
}