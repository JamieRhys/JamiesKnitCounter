package com.sycosoft.jkc.ui.pages

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.sycosoft.jkc.R
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.ui.components.lists.HomePageListItem
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.viewmodels.HomePageViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/** The Home Page view for the user to be able to see each project they have created and be able to add
 * a new project. It also provides additional information about overall stats for the projects contained
 * within the app database.
 *
 * @property viewModel The [HomePageViewModel] object the page will use to interact with the database as well as provide data to the UI.
 * @property nav The [NavController] object the page will use to navigate to other pages.
 *
 * @author Jamie-Rhys Edwards
 * @since v0.0.1
 */
@Composable
fun HomePage(
    viewModel: HomePageViewModel,
    nav: NavController,
) {
    // The project list object which observes the view model object. This will react when the other one does to any database changes.
    val projectList = viewModel.projectList.collectAsState()
    // The local context used to display Toast messages. (This should be removed once all features are implemented.
    val context = LocalContext.current

    // The dialog variables.
    var showDeleteDialog by remember { mutableStateOf(false) }
    var projectToDelete by remember { mutableLongStateOf(0L) }
    var projectToDeleteName by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            // When the user taps this button, it will navigate to the add project page.
            FloatingActionButton(
                onClick = {
                    nav.navigate(route = "add_project_page")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.button_add))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row {
                // The ProjectList will display all projects available in the database and allow the user to
                // select one to open, edit or delete.
                ProjectList(
                    projectList = projectList.value,
                    onClicked = { id ->
                        nav.navigate(route = "project_page/$id")
                    },
                    onDeleteButtonPressed = { projectId, projectName ->
                        projectToDelete = projectId
                        projectToDeleteName = projectName

                        showDeleteDialog = true
                    },
                    onEditButtonPressed = {
                        Toast.makeText(context, "Item Edited", Toast.LENGTH_SHORT).show()
                    },
                )
            }
        }

        // If the showDeleteDialog flag changes to true, display the dialog to confirm if the user
        // wants to delete the project.
        if(showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = stringResource(id = R.string.label_delete_project, projectToDeleteName)) },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deleteProject(projectToDelete)
                        projectToDelete = 0L
                        projectToDeleteName = ""
                        showDeleteDialog = false
                    }) {
                        Text(text = stringResource(id = R.string.button_yes))
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        projectToDelete = 0L
                        projectToDeleteName = ""
                        showDeleteDialog = false
                    }) {
                        Text(text = stringResource(id = R.string.button_no))
                    }
                }
            )
        }
    }
}

@Composable
private fun ProjectList(
    projectList: List<Project>,
    onClicked: (Long) -> Unit,
    onDeleteButtonPressed: (Long, String) -> Unit,
    onEditButtonPressed: (Long) -> Unit,
) {
    LazyColumn {
        items(projectList.size) { index ->
            HomePageListItem(
                name = projectList[index].name,
                onClicked = {
                    onClicked(projectList[index].id)
                },
                onDeleteButtonPressed = {
                    val project = projectList[index]
                    onDeleteButtonPressed(project.id, project.name)
                },
                onEditButtonPressed = {
                    onEditButtonPressed(projectList[index].id)
                },
            )
        }
    }
}

// region Previews
@Preview(showSystemUi = true, showBackground = true, name = "Light Mode")
@Composable
private fun LM_HomePagePreview() {
    val context = LocalContext.current
    JKCTheme {
        Surface {
            HomePage(
                viewModel = HomePageViewModel(
                    AppRepository(
                        projectDao = AppDatabase.getDatabase(context).projectDao,
                        counterDao = AppDatabase.getDatabase(context).counterDao,
                        partDao = AppDatabase.getDatabase(context).partDao,
                    )
                ),
                nav = NavController(context)
            )
        }
    }
}

@Preview(name = "Dark Mode", showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DM_HomePagePreview() {
    val context = LocalContext.current
    JKCTheme {
        Surface {
            HomePage(
                viewModel = HomePageViewModel(
                    AppRepository(
                        projectDao = AppDatabase.getDatabase(context).projectDao,
                        counterDao = AppDatabase.getDatabase(context).counterDao,
                        partDao = AppDatabase.getDatabase(context).partDao,
                    )
                ),
                nav = NavController(context)
            )
        }
    }
}
// endregion