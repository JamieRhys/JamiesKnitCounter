package com.sycosoft.jkc.ui.pages

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.ui.sections.projectpage.ProjectPageLoadingContent
import com.sycosoft.jkc.ui.sections.projectpage.ProjectPageMainContent
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.util.LoadingState
import com.sycosoft.jkc.viewmodels.ProjectPageViewModel

@Composable
fun ProjectPage(
    nav: NavController,
    viewModel: ProjectPageViewModel,
) {
    val project by viewModel.project.collectAsState()
    val parts by viewModel.parts.collectAsState()
    val activePart by viewModel.activePart.collectAsState()
    val counters by viewModel.counters.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            when(loadingState) {
                LoadingState.Loading -> {
                    ProjectPageLoadingContent()
                }
                LoadingState.Success -> {}
                LoadingState.Failure -> {
                    Text(text = "Could not load project. Returning to Home Page.")
                }
                LoadingState.Idle -> {
                    if(counters.isNotEmpty()) {
                        ProjectPageMainContent(
                            projectName = project?.name!!,
                            timer = "00:00:00",
                            counters = counters,
                            onBackButtonPressed = { nav.popBackStack() },
                            onCounterIncrementButtonPressed = { index -> viewModel.incrementCounter(index) },
                            onCounterDecrementButtonPressed = { index -> viewModel.decrementCounter(index) },
                            onCounterGloballyLinkedButtonPressed = { index -> viewModel.toggleGloballyLinked(index) },
                        )
                    } else {
                        // TODO: Handle when there are no counters.
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
@Composable
private fun LM_ProjectPagePreview() {
    val context = LocalContext.current
    JKCTheme {
        Surface {
            ProjectPage(
                nav = NavController(LocalContext.current),
                viewModel = ProjectPageViewModel(
                    AppRepository(
                        projectDao = AppDatabase.getDatabase(context).projectDao,
                        counterDao = AppDatabase.getDatabase(context).counterDao,
                        partDao = AppDatabase.getDatabase(context).partDao,
                    ),
                    projectId = 1L,
                )
            )
        }
    }
}

@Preview(name = "Dark Mode", showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DM_ProjectPagePreview() {
    val context = LocalContext.current
    JKCTheme {
        Surface {
            ProjectPage(
                nav = NavController(LocalContext.current),
                viewModel = ProjectPageViewModel(
                    AppRepository(
                        projectDao = AppDatabase.getDatabase(context).projectDao,
                        counterDao = AppDatabase.getDatabase(context).counterDao,
                        partDao = AppDatabase.getDatabase(context).partDao,
                    ),
                    projectId = 1L,
                )
            )
        }
    }
}