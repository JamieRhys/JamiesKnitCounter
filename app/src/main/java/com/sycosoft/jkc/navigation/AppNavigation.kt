package com.sycosoft.jkc.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.ui.views.AddProjectPage
import com.sycosoft.jkc.ui.views.HomePage
import com.sycosoft.jkc.viewmodels.AddProjectPageViewModel
import com.sycosoft.jkc.viewmodels.HomePageViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    app: Application
) {

    val appRepository = AppRepository(app)
    appRepository.init()
    val homePageViewModel = HomePageViewModel(appRepository)
    val addProjectPageViewModel = AddProjectPageViewModel(appRepository)

    NavHost(
        navController = navController,
        startDestination = NavigationDestination.HomePage.route
    ) {
        composable(
            route = NavigationDestination.HomePage.route,
        ) {
            HomePage(
                navController = navController,
                viewModel = homePageViewModel,
            )
        }
        composable(
            route = NavigationDestination.AddProjectPage.route,
        ) {
            AddProjectPage(
                navController = navController,
                viewModel = addProjectPageViewModel
            )
        }
    }
}