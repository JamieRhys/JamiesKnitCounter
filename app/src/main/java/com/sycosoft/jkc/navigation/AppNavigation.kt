package com.sycosoft.jkc.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.ui.views.AddProjectPage
import com.sycosoft.jkc.ui.views.HomePage
import com.sycosoft.jkc.ui.views.ProjectPage
import com.sycosoft.jkc.viewmodels.AddProjectPageViewModel
import com.sycosoft.jkc.viewmodels.HomePageViewModel
import com.sycosoft.jkc.viewmodels.ProjectPageViewModel

/** Controls the apps navigation between each page.
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    app: Application
) {

    // Declare the apps repository and then initialise it.
    val appRepository = AppRepository(app)
    appRepository.init()

    // Initialise all of the view models we are using.
    val homePageViewModel = HomePageViewModel(appRepository)
    val addProjectPageViewModel = AddProjectPageViewModel(appRepository)
    val projectPageViewModel = ProjectPageViewModel(appRepository)

    // The key for the project id.
    val projectIdKey = "projectId"

    NavHost(
        navController = navController,
        startDestination = NavigationDestination.HomePage.route
    ) {
        composable(route = NavigationDestination.HomePage.route) {
            HomePage(
                navController = navController,
                viewModel = homePageViewModel,
            )
        }
        composable(route = NavigationDestination.AddProjectPage.route) {
            AddProjectPage(
                navController = navController,
                viewModel = addProjectPageViewModel
            )
        }
        composable(
            route = NavigationDestination.ProjectPage.route.plus("/{$projectIdKey}"),
            arguments = listOf(
                navArgument(projectIdKey) {
                    type = NavType.LongType
                }
            )
        ) { navStackBackEntry ->
            // Get the project id from the back stack. If this is null, assign a value of 0.
            val projectId = navStackBackEntry.arguments?.getLong(projectIdKey) ?: 0L
            projectPageViewModel.init(projectId)

            // Pass the project ID to the page.
            ProjectPage(
                navController = navController,
                viewModel = projectPageViewModel,
            )
        }
    }
}