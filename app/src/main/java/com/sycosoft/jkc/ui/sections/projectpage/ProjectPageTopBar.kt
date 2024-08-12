package com.sycosoft.jkc.ui.sections.projectpage

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun ProjectPageTopBar(
    projectName: String?,
    timer: String?,
    onBackButtonPressed: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackButtonPressed,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null,
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, top = 8.dp, bottom = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp,
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = projectName ?: "No project selected")
                Text(text = timer ?:"00:00:00")
            }
        }
    }
}

@Preview(name = "Top Bar", showBackground = true)
@Composable
private fun LM_ProjectPageTopBarPreview() {
    JKCTheme {
        Surface {
            ProjectPageTopBar(
                projectName = "Project Name",
                timer = "00:00:00",
                onBackButtonPressed = {}
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
            ProjectPageTopBar(
                projectName = "Project Name",
                timer = "00:00:00",
                onBackButtonPressed = {},
            )
        }
    }
}