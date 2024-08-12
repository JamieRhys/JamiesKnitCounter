package com.sycosoft.jkc

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.navigation.AppNavigation
import com.sycosoft.jkc.ui.pages.AddCounterPage
import com.sycosoft.jkc.ui.pages.AddProjectPage
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.viewmodels.AddCounterPageViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JKCApp(app = this.application)
        }
    }
}

@Composable
fun JKCApp(
    app: Application
) {
    JKCTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            AppNavigation(app = app)
        }
    }
}