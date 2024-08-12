package com.sycosoft.jkc.ui.pages

import android.content.res.Configuration
import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sycosoft.jkc.R
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.ui.sections.addprojectpage.QuestionSection
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.util.LoadingState
import com.sycosoft.jkc.util.ProjectType
import com.sycosoft.jkc.viewmodels.AddProjectPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectPage(
    nav: NavController,
    viewModel: AddProjectPageViewModel,
) {
    // Declarations to hold our project values which will then be passed on to our viewmodel
    // for processing.
    var projectName by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var projectType by remember { mutableStateOf(ProjectType.Knitting) }

    // Flag to allow us to keep track of whether the form has been edited at any point. If it has and
    // the user then tries to go back to the previous page, a confirmation dialog will show. Otherwise,
    // we will just return to the previous page.
    var hasBeenEdited by remember { mutableStateOf(false) }

    val loadingState by viewModel.loadingState.collectAsState()

    // Flags for each of our dialogs which can be shown to the user.
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showFailureDialog by remember { mutableStateOf(false) }
    var showEditedDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_add_project)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // Check to see if the form has been edited. If it has, we will mark
                            // the showEditedDialog to true so the corresponding dialog will show.
                            if(hasBeenEdited) {
                                showEditedDialog = true
                            } else {
                                // It's not been edited? OK, let's just move back to the previous page.
                                nav.popBackStack()
                            }
                        },
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = stringResource(R.string.button_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            when(loadingState) {
                LoadingState.Loading -> CircularProgressIndicator()
                LoadingState.Success -> {
                    Text(text = stringResource(id = R.string.label_project_added_success))
                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = { nav.popBackStack() }
                    ) {
                        Text(text = stringResource(id = R.string.button_ok))
                    }
                }
                LoadingState.Failure -> {
                    Text(text = stringResource(id = R.string.label_project_added_failure))
                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = { nav.popBackStack() }
                    ) {
                        Text(text = stringResource(id = R.string.button_ok))
                    }
                }
                LoadingState.Idle -> {
                    QuestionSection(
                        projectName = projectName,
                        onProjectNameChange = { projectName = it },
                        projectDescription = projectDescription,
                        onProjectDescriptionChange = { projectDescription = it },
                        projectType = projectType,
                        onProjectTypeChange = {
                            projectType = it

                            // Here's the other check to see if it's been edited. If so, change the hasBeenEdited
                            // flag.
                            if(projectType != ProjectType.Knitting) {
                                hasBeenEdited = true
                            }
                        },
                    )

                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            validateInputs(
                                projectName = projectName,
                                onProjectNameChange = { projectName = it },
                                projectDescription = projectDescription,
                                onProjectDescriptionChange = { projectDescription = it },
                            )

                            viewModel.addProject(
                                projectName = projectName,
                                projectDescription = projectDescription,
                                projectType = projectType,
                            )
                        }
                    ) {
                        Text(text = stringResource(id = R.string.button_add))
                    }
                }
            }
        }

        Dialogs(
            showSuccessDialog = showSuccessDialog,
            showFailureDialog = showFailureDialog,
            showEditedDialog = showEditedDialog,
            onShowEditedDialogChange = { showEditedDialog = it },
            onEditedDialogConfirmClick = { nav.popBackStack() },
        )
    }
}

@Composable
private fun Dialogs(
    showSuccessDialog: Boolean,
    showFailureDialog: Boolean,
    showEditedDialog: Boolean,
    onShowEditedDialogChange: (Boolean) -> Unit,
    onEditedDialogConfirmClick: () -> Unit,
) {
    if(showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = stringResource(id = R.string.title_success)) },
            text = { Text(text = stringResource(id = R.string.label_project_added_success)) },
            confirmButton = {
                Button(
                    onClick = { /* TODO */ },
                ) {
                    Text(text = stringResource(id = R.string.button_ok))
                }
            }
        )
    }

    if(showFailureDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = stringResource(id = R.string.title_success)) },
            text = { Text(text = stringResource(id = R.string.label_project_added_failure)) },
            confirmButton = {
                Button(
                    onClick = { /* TODO */ },
                ) {
                    Text(text = stringResource(id = R.string.button_ok))
                }
            }
        )
    }

    if(showEditedDialog) {
        AlertDialog(
            onDismissRequest = { onShowEditedDialogChange(false) },
            title = { Text(text = stringResource(id = R.string.title_confirm)) },
            text = { Text(text = stringResource(id = R.string.text_project_edited)) },
            confirmButton = {
                Button(
                    onClick = {
                        onEditedDialogConfirmClick()
                    }
                ) {
                    Text(text = stringResource(id = R.string.button_yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = { onShowEditedDialogChange(false) },
                ) {
                    Text(text = stringResource(id = R.string.button_no))
                }
            }
        )
    }
}

private fun validateInputs(
    projectName: String,
    onProjectNameChange: (String) -> Unit,
    projectDescription: String,
    onProjectDescriptionChange: (String) -> Unit,
) {
    // Let's start with the project name first
    // TODO: Get number of projects so we can increment the project name.
    if(projectName.isNotBlank()) { onProjectNameChange(projectName.trim()) }

    // Now let's check if the project description has been entered. If's not been edited, we can
    // leave it blank as it's not required.
    if(projectDescription.isNotBlank()) {
        // If it is, we need to trim it to ensure it's what we're expecting.
        onProjectDescriptionChange(projectDescription.trim())
    }
}

// region Preview Methods

@Preview(showBackground = true, showSystemUi = true, name = "Light Mode")
@Composable
private fun LM_AddProjectPagePreview() {
    val context = LocalContext.current
    JKCTheme {
        Surface {
            AddProjectPage(
                nav = rememberNavController(),
                viewModel = AddProjectPageViewModel(AppRepository(
                    projectDao = AppDatabase.getDatabase(context).projectDao,
                    counterDao = AppDatabase.getDatabase(context).counterDao,
                    partDao = AppDatabase.getDatabase(context).partDao,
                ))
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DM_AddProjectPagePreview() {
    val context = LocalContext.current
    JKCTheme {
        Surface {
            AddProjectPage(
                nav = rememberNavController(),
                viewModel = AddProjectPageViewModel(AppRepository(
                    projectDao = AppDatabase.getDatabase(context).projectDao,
                    counterDao = AppDatabase.getDatabase(context).counterDao,
                    partDao = AppDatabase.getDatabase(context).partDao,
                ))
            )
        }
    }
}

// endregion