package com.sycosoft.jkc.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sycosoft.jkc.ui.theme.JKCTheme

@Composable
fun ProjectStat(
    count: String,
    title: String,
) {
    Column(
        modifier = Modifier
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(count)
        Text(title)
    }
}

@Preview
@Composable
private fun PreviewProjectStat() {
    JKCTheme {
        Surface {
            ProjectStat(
                count = "0",
                title = "Stitches"
            )
        }
    }
}