package com.sycosoft.jkc.ui.components.headers

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sycosoft.jkc.R
import com.sycosoft.jkc.ui.components.ProjectStat
import com.sycosoft.jkc.ui.theme.JKCTheme

@Composable
fun HomePageHeader(
    stitchCount: String,
    rowCount: String,
    completedCount: String,
    timeSpent: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ProjectStat(count = stitchCount, title = stringResource(id = R.string.label_stitches))      // Overall stitches for all projects in database.
            ProjectStat(count = rowCount, title = stringResource(id = R.string.label_rows))             // Overall rows for all projects in database.
            ProjectStat(count = completedCount, title = stringResource(id = R.string.label_completed))  // Overall projects completed.
            ProjectStat(count = timeSpent, title = stringResource(id = R.string.label_time))            // Overall time spent on all projects.
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
private fun LM_HomePageHeaderPreview() {
    JKCTheme {
        Surface {
            HomePageHeader(
                stitchCount = "0",
                rowCount = "0",
                completedCount = "0",
                timeSpent = "0:00",
            )
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DM_HomePageHeaderPreview() {
    JKCTheme {
        Surface {
            HomePageHeader(
                stitchCount = "0",
                rowCount = "0",
                completedCount = "0",
                timeSpent = "0:00",
            )
        }
    }
}