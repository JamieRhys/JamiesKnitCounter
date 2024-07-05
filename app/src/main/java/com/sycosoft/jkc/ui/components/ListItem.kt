package com.sycosoft.jkc.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sycosoft.jkc.ui.theme.JKCTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    name: String,
    onEditButtonPressed: (() -> Unit),
    onDeleteButtonPressed: (() -> Unit),
) {
    var showButtons by remember { mutableStateOf(false) }
    val transitionState = remember { MutableTransitionState(showButtons) }
    transitionState.targetState = showButtons

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .combinedClickable(
                onClick = {
                    Log.d("ListItem", "$name clicked.")
                    onEditButtonPressed()
                },
                onLongClick = {
                    //onDeleteButtonPressed()
                    showButtons = !showButtons
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.padding(
                    start = 25.dp,
                    top = 20.dp,
                    bottom = 20.dp,
                )
            ) {
                Text(text = name)
            }
            AnimatedVisibility(
                visibleState = transitionState,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it }),
            ) {
                Row(
                    modifier = Modifier.padding(end = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onEditButtonPressed) {
                        Icon(
                            Icons.Default.Edit,
                            tint = Color.Cyan,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = onDeleteButtonPressed) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                    IconButton(onClick = { showButtons = !showButtons }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LM_ListItem() {
    JKCTheme {
        Surface {
            ListItem(
                name = "Project",
                onEditButtonPressed = {},
                onDeleteButtonPressed = {},
            )
        }
    }
}

@Preview
@Composable
private fun DM_ListItem() {
    JKCTheme {
        Surface {
            ListItem(
                name = "Project",
                onEditButtonPressed = {},
                onDeleteButtonPressed = {},
            )
        }
    }
}