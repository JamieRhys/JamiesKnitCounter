package com.sycosoft.jkc.ui.sections.projectpage

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.ui.components.lists.CounterListItem
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.ui.theme.pastelBlue
import com.sycosoft.jkc.util.CounterType

@Composable
fun ProjectPageMainContent(
    projectName: String,
    timer: String,
    counters: List<Counter>,
    onBackButtonPressed: () -> Unit,
    onCounterIncrementButtonPressed: (Int) -> Unit,
    onCounterDecrementButtonPressed: (Int) -> Unit,
    onCounterGloballyLinkedButtonPressed: (Int) -> Unit,
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            ProjectPageTopBar(
                projectName = projectName,
                timer = timer,
                onBackButtonPressed = { onBackButtonPressed() }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TopCounter(
                counterName = counters[0].name,
                counterValue = counters[0].value.toString(),
                onIncrementButtonClick = { onCounterIncrementButtonPressed(0) },
                onDecrementButtonClick = { onCounterDecrementButtonPressed(0) }
            )
            TopCounter(
                counterName = counters[1].name,
                counterValue = counters[1].value.toString(),
                onIncrementButtonClick = { onCounterIncrementButtonPressed(1) },
                onDecrementButtonClick = { onCounterDecrementButtonPressed(1) }
            )
        }
        //HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(8.dp)
                .background(
                    color = pastelBlue,
                    shape = RoundedCornerShape(12.dp),
                ),
        ) {
            LazyColumn(
                modifier = Modifier.padding(top =  16.dp)
            ) {
                items(counters.size) { index ->
                    val counter = counters[index]

                    if(counter.counterType != CounterType.Global && counter.counterType != CounterType.Stitch) {
                        CounterListItem(
                            name = counter.name,
                            value = counter.value.toString(),
                            isGloballyLinked = counter.isGloballyLinked,
                            onIncrementButtonPressed = { onCounterIncrementButtonPressed(index) },
                            onDecrementButtonPressed = { onCounterDecrementButtonPressed(index) },
                            onGloballyLinkedButtonPressed = { onCounterGloballyLinkedButtonPressed(index) }
                        )
                    }
                }
            }
        }
        ProjectPageBottomBar(
            partName = "Part Name",
            onPartChangeButtonPressed = { /*TODO*/ },
            onTimerButtonPressed = { /*TODO*/ },
            onAddButtonPressed = { /*TODO*/ },
        )
    }

}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun LightPreview() {
    JKCTheme {
        Surface {
            ProjectPageMainContent(
                projectName = "Test Project",
                timer = "00:00:00",
                counters = listOf(
                    Counter(
                        id = 1,
                        name = "Global",
                        value = 0,
                        counterType = CounterType.Global,
                        owningPartId = 1,
                        isGloballyLinked = false,
                    ),
                    Counter(
                        id = 1,
                        name = "Stitch",
                        value = 0,
                        counterType = CounterType.Stitch,
                        owningPartId = 1,
                        isGloballyLinked = false,
                    ),
                    Counter(
                        id = 1,
                        name = "Counter 1",
                        value = 0,
                        counterType = CounterType.Normal,
                        owningPartId = 1,
                        isGloballyLinked = true,
                    ),
                    Counter(
                        id = 1,
                        name = "Counter 2",
                        value = 0,
                        counterType = CounterType.Normal,
                        owningPartId = 1,
                        isGloballyLinked = false,
                    ),
                ),
                onBackButtonPressed = {},
                onCounterIncrementButtonPressed = {},
                onCounterDecrementButtonPressed = {},
                onCounterGloballyLinkedButtonPressed = {},
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
            ProjectPageMainContent(
                projectName = "Test Project",
                timer = "00:00:00",
                counters = listOf(
                    Counter(
                        id = 1,
                        name = "Global",
                        value = 0,
                        counterType = CounterType.Global,
                        owningPartId = 1,
                        isGloballyLinked = false,

                        ),
                    Counter(
                        id = 1,
                        name = "Stitch",
                        value = 0,
                        counterType = CounterType.Stitch,
                        owningPartId = 1,
                        isGloballyLinked = false,
                    ),
                    Counter(
                        id = 1,
                        name = "Counter 1",
                        value = 0,
                        counterType = CounterType.Normal,
                        owningPartId = 1,
                        isGloballyLinked = true,
                    ),
                    Counter(
                        id = 1,
                        name = "Counter 2",
                        value = 0,
                        counterType = CounterType.Normal,
                        owningPartId = 1,
                        isGloballyLinked = false,
                    ),
                ),
                onBackButtonPressed = {},
                onCounterIncrementButtonPressed = {},
                onCounterDecrementButtonPressed = {},
                onCounterGloballyLinkedButtonPressed = {},
            )
        }
    }
}