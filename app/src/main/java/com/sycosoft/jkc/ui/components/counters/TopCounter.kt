package com.sycosoft.jkc.ui.components.counters

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sycosoft.jkc.ui.theme.JKCTheme

@Composable
fun TopCounter(
    title: String,
    value: String,
    onIncrementButtonPressed: () -> Unit,
    onDecrementButtonPressed: () -> Unit,
) {
    Card(
        modifier = Modifier.padding(5.dp),
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(title)
            }
            Row {
                FloatingActionButton(
                    modifier = Modifier.padding(end = 5.dp),
                    onClick = { onIncrementButtonPressed() }
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                }
                FloatingActionButton(
                    modifier = Modifier.padding(start = 5.dp),
                    onClick = { onDecrementButtonPressed() }
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }
        }

    }
}

@Preview(name = "Light Mode")
@Composable
private fun LM_ProjectPage() {
    JKCTheme {
        Surface {
            TopCounter(
                title = "Global",
                value = "0",
                onIncrementButtonPressed = {},
                onDecrementButtonPressed = {}
            )
        }
    }
}

@Preview(name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DM_ProjectPage() {
    JKCTheme {
        Surface {
            TopCounter(
                title = "Global",
                value = "0",
                onIncrementButtonPressed = {},
                onDecrementButtonPressed = {}
            )
        }
    }
}