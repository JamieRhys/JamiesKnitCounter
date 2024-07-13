package com.sycosoft.jkc.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.viewmodels.AddCounterPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCounterPage(
    navController: NavHostController,
    viewModel: AddCounterPageViewModel,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Counter")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
@Composable
private fun LM_AddCounterPage() {
    JKCTheme {
        Surface {
            AddCounterPage(
                navController = rememberNavController(),
                viewModel = AddCounterPageViewModel(AppRepository(LocalContext.current))
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    name = "Dark Mode"
)
@Composable
private fun DM_AddCounterPage() {
    JKCTheme {
        Surface {
            AddCounterPage(
                navController = rememberNavController(),
                viewModel = AddCounterPageViewModel(AppRepository(LocalContext.current))
            )
        }
    }
}