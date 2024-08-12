package com.sycosoft.jkc.ui.sections.projectpage

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Watch
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
fun ProjectPageBottomBar(
    partName: String?,
    onPartChangeButtonPressed: () -> Unit,
    onTimerButtonPressed: () -> Unit,
    onAddButtonPressed: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Card(
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = partName ?: "No part selected",
                )
            }
        }
        Row {
            IconButton(
                onClick = onPartChangeButtonPressed,
            ) {
                Icon(
                    imageVector = Icons.Default.ChangeCircle,
                    contentDescription = null,
                )
            }
            IconButton(
                onClick = onTimerButtonPressed,
            ) {
                Icon(
                    imageVector = Icons.Default.Watch,
                    contentDescription = null,
                )
            }
            IconButton(
                onClick = onAddButtonPressed,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview(name = "Top Bar", showBackground = true)
@Composable
private fun LM_ProjectPageTopBarPreview() {
    JKCTheme {
        Surface {
            ProjectPageBottomBar(
                partName = "Part 1",
                onPartChangeButtonPressed = {},
                onTimerButtonPressed = {},
                onAddButtonPressed = {},
            )
        }
    }
}

@Preview(name = "Top Bar", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DM_ProjectPageTopBarPreview() {
    JKCTheme {
        Surface {
           ProjectPageBottomBar(
               partName = "Part 1",
               onPartChangeButtonPressed = {},
               onTimerButtonPressed = {},
               onAddButtonPressed = {},
           )
        }
    }
}