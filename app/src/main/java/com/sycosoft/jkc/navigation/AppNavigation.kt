package com.sycosoft.jkc.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.ui.pages.AddCounterPage
import com.sycosoft.jkc.ui.pages.AddProjectPage
import com.sycosoft.jkc.ui.pages.HomePage
import com.sycosoft.jkc.ui.pages.ProjectPage
import com.sycosoft.jkc.viewmodels.AddCounterPageViewModel
import com.sycosoft.jkc.viewmodels.AddProjectPageViewModel
import com.sycosoft.jkc.viewmodels.HomePageViewModel
import com.sycosoft.jkc.viewmodels.ProjectPageViewModel

/** This method is responsible for handling navigation within the application.
 *
 * @property navController The navigation controller for the application.
 * @property app The application instance.
 *
 * @author Jamie-Rhys Edwards
 * @since v0.0.1
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    app: Application,
) {
    val database = AppDatabase.getDatabase(app)
    val appRepository = AppRepository(
        projectDao = database.projectDao,
        counterDao = database.counterDao,
        partDao = database.partDao,
    )

    val projectIdKey = "projectId"

    // Navigation
    NavHost(
        navController = navController,
        startDestination = NavigationDestination.HomePage.route,
    ) {
        composable(route = NavigationDestination.HomePage.route) {
            HomePage(
                viewModel = HomePageViewModel(appRepository = appRepository),
                nav = navController,
            )
        }
        composable(route = NavigationDestination.AddProjectPage.route) {
            AddProjectPage(
                nav = navController,
                viewModel = AddProjectPageViewModel(appRepository = appRepository),
            )
        }
        composable(
            route = NavigationDestination.ProjectPage.route.plus("/{$projectIdKey}"),
            arguments = listOf(
                navArgument(projectIdKey) { type = NavType.LongType }
            )
        ) { navBackStackEntry ->
            val projectId = navBackStackEntry.arguments?.getLong(projectIdKey) ?: 0L

            ProjectPage(
                nav = navController,
                viewModel = ProjectPageViewModel(
                    appRepository = appRepository,
                    projectId = projectId,
                )
            )
        }
    }
}