package com.sycosoft.jkc.ui.sections.projectpage

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.util.CounterType

@Composable
fun TopCounter(
    counterName: String?,
    counterValue: String?,
    onIncrementButtonClick: (() -> Unit),
    onDecrementButtonClick: (() -> Unit),
) {
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            IconButton(onClick = { onIncrementButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = counterName ?: "Counter")
                Text(text = counterValue ?: "0")
            }
            IconButton(onClick = { onDecrementButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
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
            TopCounter(
                counterName = "Global",
                counterValue = "0",
                onIncrementButtonClick = {},
                onDecrementButtonClick = {},
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
            TopCounter(
                counterName = "Global",
                counterValue = "0",
                onIncrementButtonClick = {},
                onDecrementButtonClick = {},
            )
        }
    }
}