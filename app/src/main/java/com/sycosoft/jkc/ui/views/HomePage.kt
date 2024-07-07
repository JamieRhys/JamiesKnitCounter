package com.sycosoft.jkc.ui.views

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sycosoft.jkc.R
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.navigation.NavigationDestination
import com.sycosoft.jkc.ui.components.ListItem
import com.sycosoft.jkc.ui.components.ProjectStat
import com.sycosoft.jkc.ui.components.headers.HomePageHeader
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.viewmodels.HomePageViewModel

/**
 * The Home Page view. This will display any information relating to all projects, and allows the
 * user to select a project they are working on. It will then pass this information over to the
 * [ProjectPage] to display.
 */
@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: HomePageViewModel,
) {
    viewModel.refreshData()
    val projectList = viewModel.projectList.collectAsState().value

    // Dictates if the delete dialog is to be shown or not.
    var showDeleteDialog by remember { mutableStateOf(false) }
    // Holds the id of the project to be deleted.
    var projectToDelete by remember { mutableLongStateOf(0) }
    // Holds the name of the project to be deleted.
    var projectNameToDelete by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavigationDestination.AddProjectPage.route)
                },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HomePageHeader(
                    stitchCount = "0",
                    rowCount = "0",
                    completedCount = "0",
                    timeSpent = "0:00",
                )
            }

            Row {
                // Shows each project item held in the database.
                LazyColumn() {
                    items(projectList.size) { project ->
                        ListItem(
                            name = projectList[project].projectName,
                            onClicked = {
                                navController.navigate(NavigationDestination.ProjectPage.route.plus("/${projectList[project].id}"))
                            },
                            onEditButtonPressed = {  },
                            onDeleteButtonPressed = {
                                projectToDelete = projectList[project].id               // Get the id of the project to be deleted.
                                projectNameToDelete = projectList[project].projectName  // Get the name of the project to be deleted.
                                showDeleteDialog = true                                 // show the delete dialog to confirm deletion.
                            },
                        )
                    }
                }
            }

            // Delete dialog
            if(showDeleteDialog) {
                // Check to make sure the project to be deleted is not 0.
                if(projectToDelete != 0L) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = !showDeleteDialog },
                        title = { Text(stringResource(R.string.title_confirm)) },
                        text = { Text(stringResource(R.string.label_delete_project, projectNameToDelete)) },
                        confirmButton = {
                            Button(
                                colors = ButtonColors(
                                    containerColor =  Color.Red,
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.LightGray,
                                    disabledContentColor = Color.DarkGray,
                                ),
                                onClick = {
                                    // Delete project from the database
                                    viewModel.removeProject(projectToDelete)
                                    showDeleteDialog = !showDeleteDialog
                                }
                            ) {
                                Text(stringResource(R.string.button_confirm))
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDeleteDialog = !showDeleteDialog },

                            ) {
                                Text(stringResource(R.string.button_cancel))
                            }
                        },
                    )
                }

                Log.i("HomePage", "Project to delete: $showDeleteDialog")
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
private fun LM_HomePagePreview() {
    JKCTheme {
        Surface {
            HomePage(
                navController = rememberNavController(),
                viewModel = HomePageViewModel(AppRepository(LocalContext.current)),
            )
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DM_HomePagePreview() {
    JKCTheme {
        Surface {
            HomePage(
                navController = rememberNavController(),
                viewModel = HomePageViewModel(AppRepository(LocalContext.current)),
            )
        }
    }
}