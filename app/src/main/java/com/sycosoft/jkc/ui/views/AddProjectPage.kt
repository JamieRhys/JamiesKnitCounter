package com.sycosoft.jkc.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sycosoft.jkc.R
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.viewmodels.AddProjectPageViewModel

/**
 * Page view for adding a new project to the database. Allowing the user to start a new project.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectPage(
    navController: NavController,
    viewModel: AddProjectPageViewModel
) {
    var projectName by rememberSaveable { mutableStateOf("") }
    val result by viewModel.result.collectAsState()

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text(text = stringResource(id = R.string.title_add_project))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = projectName,
                onValueChange = { projectName = it },
                label = { Text(text = stringResource( id = R.string.label_project_name)) }
            )
            Button(onClick = {
                // Add validation here.
                viewModel.saveProject(projectName)
            }) {
                Text(text = "Add")
            }

            if(result == AddProjectPageViewModel.Companion.Result.Success) {
                AlertDialog(
                    onDismissRequest = {  },
                    title = { Text(stringResource(R.string.title_add_project)) },
                    text = { Text(stringResource(R.string.label_project_added_success)) },
                    confirmButton = {
                        Button(onClick = {
                            navController.popBackStack()
                        }) {
                            Text(stringResource(R.string.button_ok))
                        }
                    }
                )
            }

            if(result == AddProjectPageViewModel.Companion.Result.Loading) {
                CircularProgressIndicator()
            }

            if(result == AddProjectPageViewModel.Companion.Result.Failure) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(stringResource(R.string.title_failure)) },
                    text = { Text(stringResource(R.string.label_project_added_failure)) },
                    confirmButton = {
                        Button(onClick = {
                            navController.popBackStack()
                        }) {
                            Text(stringResource(R.string.button_ok))
                        }
                    }
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
@Composable
private fun LM_AddProjectPage() {
    JKCTheme {
        Surface {
            AddProjectPage(
                navController = rememberNavController(),
                viewModel = AddProjectPageViewModel(AppRepository(LocalContext.current))
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    name = "Dark Mode"
)
@Composable
private fun DM_AddProjectPage() {
    JKCTheme {
        Surface {
            AddProjectPage(
                navController = rememberNavController(),
                viewModel = AddProjectPageViewModel(AppRepository(LocalContext.current))
            )
        }
    }
}