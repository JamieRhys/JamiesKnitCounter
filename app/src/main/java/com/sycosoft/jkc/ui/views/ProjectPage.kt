package com.sycosoft.jkc.ui.views

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sycosoft.jkc.R
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.ui.components.counters.TopCounter
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.util.LoadingState
import com.sycosoft.jkc.viewmodels.ProjectPageViewModel

@Composable
fun ProjectPage(
    navController: NavHostController,
    viewModel: ProjectPageViewModel,
) {
    // Controls if we need to see the invalid project ID dialog.
    val showInvalidIdDialog by remember { mutableStateOf(viewModel.projectId.value == 0L) }

    val counterList = viewModel.counterList.collectAsState().value

    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                }
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
            // If the project ID is invalid, show a dialog to the user and then return to the home page.
            if(showInvalidIdDialog) InvalidIdDialog(navController = navController)

            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)) {
                            Text(
                                text = "Project Page",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                modifier = Modifier.padding(5.dp),
                                onClick = {
                                    Toast.makeText(context, "Time Clicked", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(text = "00:00:00")
                            }
                            Button(onClick = {
                                Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "Settings")
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if(counterList.size >= 2) {
                        TopCounter(
                            title = stringResource(R.string.label_global),
                            value = counterList[0].counterValue.toString(),
                            onIncrementButtonPressed = {
                                Toast.makeText(context, "Global Increment Clicked", Toast.LENGTH_SHORT).show()
                            },
                            onDecrementButtonPressed = {
                                Toast.makeText(context, "Global Decrement Clicked", Toast.LENGTH_SHORT).show()
                            },
                        )
                        TopCounter(
                            title = stringResource(R.string.label_stitches),
                            value = counterList[1].counterValue.toString(),
                            onIncrementButtonPressed = {
                                Toast.makeText(context, "Stitches Increment Clicked", Toast.LENGTH_SHORT).show()
                            },
                            onDecrementButtonPressed = {
                                Toast.makeText(context, "Stitches Decrement Clicked", Toast.LENGTH_SHORT).show()
                            },
                        )
                    }
                }
                LazyColumn {
                    items(counterList.size) {
                        if(!counterList[it].isGlobal && !counterList[it].isStitch) {
                            TopCounter(
                                title = counterList[it].counterName,
                                value = counterList[it].counterValue.toString(),
                                onIncrementButtonPressed = {},
                                onDecrementButtonPressed = {},
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Shows a dialog when the project ID passed is invalid. It then sends the user back to [HomePage].
 */
@Composable
private fun InvalidIdDialog(navController: NavHostController) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.title_uhoh)) },
        text = { Text(stringResource(R.string.label_invalid_project_id)) },
        confirmButton = {
            Button(onClick = { navController.popBackStack() }) {
                Text(stringResource(R.string.button_ok))
            }
        }
    )
}

@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
@Composable
private fun LM_ProjectPage() {
    JKCTheme {
        Surface {
            ProjectPage(
                navController = rememberNavController(),
                viewModel = ProjectPageViewModel(AppRepository(LocalContext.current))
            )
        }
    }
}

@Preview(name = "Dark Mode", showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DM_ProjectPage() {
    JKCTheme {
        Surface {
            ProjectPage(
                navController = rememberNavController(),
                viewModel = ProjectPageViewModel(AppRepository(LocalContext.current))
            )
        }
    }
}