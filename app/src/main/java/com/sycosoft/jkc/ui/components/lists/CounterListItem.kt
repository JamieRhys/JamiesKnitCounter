package com.sycosoft.jkc.ui.components.lists

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sycosoft.jkc.ui.theme.JKCTheme

@Composable
fun CounterListItem(
    name: String,
    value: String,
    isGloballyLinked: Boolean,
    onIncrementButtonPressed: () -> Unit,
    onDecrementButtonPressed: () -> Unit,
    onGloballyLinkedButtonPressed: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp,
                ),
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onIncrementButtonPressed() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = name)
                    Text(text = value)
                }
                IconButton(onClick = { onDecrementButtonPressed() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                    )
                }
            }
        }
        IconButton(onClick = { onGloballyLinkedButtonPressed() }) {
            Icon(
                imageVector = if(isGloballyLinked) { Icons.Default.Link } else { Icons.Default.LinkOff },
                contentDescription = null,
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun LightPreview() {
    JKCTheme {
        Surface {
            CounterListItem(
                name = "Counter 1",
                value = "0",
                isGloballyLinked = true,
                onIncrementButtonPressed = {},
                onDecrementButtonPressed = {},
                onGloballyLinkedButtonPressed = {},
            )
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DarkPreview() {
    JKCTheme {
        Surface {
            CounterListItem(
                name = "Counter 1",
                value = "0",
                isGloballyLinked = true,
                onIncrementButtonPressed = {},
                onDecrementButtonPressed = {},
                onGloballyLinkedButtonPressed = {},
            )
        }
    }
}