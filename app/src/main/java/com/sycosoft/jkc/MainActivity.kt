package com.sycosoft.jkc

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sycosoft.jkc.navigation.AppNavigation
import com.sycosoft.jkc.ui.theme.JKCTheme

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