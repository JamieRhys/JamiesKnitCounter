package com.sycosoft.jkc.ui.pages

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sycosoft.jkc.R
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.ui.theme.JKCTheme
import com.sycosoft.jkc.util.LoadingState
import com.sycosoft.jkc.viewmodels.AddCounterPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCounterPage(
    onBackButtonPressed: () -> Unit,
    onAddButtonPressed: () -> Unit,
    viewModel: AddCounterPageViewModel,
) {
    var counterName by remember {mutableStateOf("") }
    var incrementCounterBy by remember { mutableStateOf("1") }
    var isGloballyLinked by remember { mutableStateOf(true) }
    var resetRow by remember { mutableStateOf("0") }
    var maxResets by remember { mutableStateOf("0") }

    var showResetRowDialog by remember { mutableStateOf(false) }
    var showMaxResetsDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showFailureDialog by remember { mutableStateOf(false) }

    val owningPartName by viewModel.owningPartName.collectAsState()
    val isLoading by viewModel.loadingState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_add_counter)) },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackButtonPressed() },
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = stringResource(R.string.button_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.label_add_counter_header, owningPartName)
            )

            QuestionSection(
                counterName = counterName,
                onCounterNameChange = { counterName = it },
                incrementCounterBy = incrementCounterBy,
                onIncrementCounterByChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() }
                    incrementCounterBy = filteredValue
                },
                resetRow = resetRow,
                onResetRowChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() }
                    resetRow = filteredValue
                },
                showResetRowDialog = { showResetRowDialog = it },
                maxResets = maxResets,
                onMaxResetChange = {
                    val filteredValue = it.filter { char -> char.isDigit() }
                    maxResets = filteredValue
                },
                showMaxResetsDialog = { showMaxResetsDialog = it },
                isGloballyLinked = isGloballyLinked,
                onIsGloballyLinkedChange = { isGloballyLinked = it },
            )

            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    ValidateInputs(
                        counterName = counterName,
                        onCounterNameChange = { counterName = it },
                        incrementCounterBy = incrementCounterBy,
                        onIncrementCounterByChange = { incrementCounterBy = it },
                        maxResets = maxResets,
                        onMaxResetChange = { maxResets = it },
                        resetRow = resetRow,
                        onResetRowChange = { resetRow = it },
                    )

                    viewModel.addCounter(
                        counterName = counterName,
                        incrementCounterBy = incrementCounterBy,
                        isGloballyLinked = isGloballyLinked,
                        resetRow = resetRow,
                        maxResets = maxResets
                    )
                }
            ) {
                Text(text = stringResource(id = R.string.button_add))
            }

            when(isLoading) {
                LoadingState.Loading -> CircularProgressIndicator()
                LoadingState.Success -> {
                    showSuccessDialog = true
                }
                LoadingState.Failure -> {
                    showFailureDialog = true
                }
                LoadingState.Idle -> {}
            }

            Dialogs(
                showResetRowDialog = showResetRowDialog,
                onShowResetDialogChange = { showResetRowDialog = it },
                showMaxResetsDialog = showMaxResetsDialog,
                onShowMaxResetsDialogChange = { showMaxResetsDialog = it },
                showSuccessDialog = showSuccessDialog,
                showFailureDialog = showFailureDialog,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestionSection(
    counterName: String,
    onCounterNameChange: (String) -> Unit,
    incrementCounterBy: String,
    onIncrementCounterByChange: (String) -> Unit,
    resetRow: String,
    onResetRowChange: (String) -> Unit,
    showResetRowDialog: (Boolean) -> Unit,
    maxResets: String,
    onMaxResetChange: (String) -> Unit,
    showMaxResetsDialog: (Boolean) -> Unit,
    isGloballyLinked: Boolean,
    onIsGloballyLinkedChange: (Boolean) -> Unit,
) {
    // Counter Name
    OutlinedTextField(
        value = counterName,
        onValueChange = onCounterNameChange,
        label = { Text(text = stringResource(id = R.string.label_counter_name)) },
        singleLine = true,
    )

    // Increment Counter By
    OutlinedTextField(
        value = incrementCounterBy,
        onValueChange = onIncrementCounterByChange,
        label = { Text(text = stringResource(id = R.string.label_counter_increments_by)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )

    // Reset Row
    OutlinedTextField(
        modifier = Modifier.padding(5.dp),
        value = resetRow,
        onValueChange = onResetRowChange,
        label = { Text(text = stringResource(id = R.string.label_counter_reset_row)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { showResetRowDialog(true) }) {
                Icon(
                    imageVector = Icons.Default.QuestionMark,
                    contentDescription = stringResource(id = R.string.button_question_mark)

                )
            }
        },
    )

    // Max Resets
    OutlinedTextField(
        modifier = Modifier.padding(5.dp),
        value = maxResets,
        onValueChange = onMaxResetChange,
        label = { Text(text = stringResource(id = R.string.label_counter_max_resets)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { showMaxResetsDialog(true) }) {
                Icon(
                    imageVector = Icons.Default.QuestionMark,
                    contentDescription = stringResource(id = R.string.button_question_mark)
                )
            }
        }
    )

    // Is Globally Linked
    Text(
        modifier = Modifier.padding(top = 5.dp),
        text = stringResource(id = R.string.label_counter_linked_global_counter)
    )
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.padding(bottom = 5.dp)
    ) {
        SegmentedButton(
            onClick = { onIsGloballyLinkedChange(true) },
            selected = isGloballyLinked,
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
        ) {
            Text(text = stringResource(id = R.string.button_yes))
        }
        SegmentedButton(
            onClick = { onIsGloballyLinkedChange(false) },
            selected = !isGloballyLinked,
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
        ) {
            Text(text = stringResource(id = R.string.button_no))
        }
    }
}

private fun ValidateInputs(
    counterName: String,
    onCounterNameChange: (String) -> Unit,
    incrementCounterBy: String,
    onIncrementCounterByChange: (String) -> Unit,
    resetRow: String,
    onResetRowChange: (String) -> Unit,
    maxResets: String,
    onMaxResetChange: (String) -> Unit,
) {
    // Let's start with the counter name first
    // TODO: Get number of part counters so we can increment the counter name if needed.
    if(counterName.isNotBlank()) { onCounterNameChange(counterName.trim()) }
    else { onCounterNameChange("Counter") }

    // Now let's check the increment counter by
    if(incrementCounterBy.isBlank()) { onIncrementCounterByChange("1") }

    // Let's now check row resets
    if(resetRow.isBlank()) { onResetRowChange("0") }

    // Now Max resets
    if(maxResets.isBlank()) { onMaxResetChange("0") }
}

@Composable
private fun Dialogs(
    showResetRowDialog: Boolean,
    onShowResetDialogChange: (Boolean) -> Unit,
    showMaxResetsDialog: Boolean,
    onShowMaxResetsDialogChange: (Boolean) -> Unit,
    showSuccessDialog: Boolean,
    showFailureDialog: Boolean,
) {
    if(showResetRowDialog) {
        AlertDialog(
            onDismissRequest = { onShowResetDialogChange(false) },
            title = { Text(text = stringResource(id = R.string.label_counter_reset_row)) },
            text = { Text(text = stringResource(id = R.string.text_reset_row_description)) },
            confirmButton = {
                Button(
                    onClick = { onShowResetDialogChange(false) },
                ) {
                    Text(text = stringResource(id = R.string.button_ok))
                }
            }
        )
    }

    if(showMaxResetsDialog) {
        AlertDialog(
            onDismissRequest = { onShowMaxResetsDialogChange(false) },
            title = { Text(text = stringResource(id = R.string.label_counter_max_resets)) },
            text = { Text(text = stringResource(id = R.string.text_max_resets_description)) },
            confirmButton = {
                Button(
                    onClick = { onShowMaxResetsDialogChange(false) },
                ) {
                    Text(text = stringResource(id = R.string.button_ok))
                }
            }
        )
    }

    if(showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = stringResource(id = R.string.title_success)) },
            text = { Text(text = stringResource(id = R.string.label_project_added_success)) },
            confirmButton = {
                Button(
                    onClick = {},
                ) {
                    Text(text = stringResource(id = R.string.button_ok))
                }
            }
        )
    }

    if(showFailureDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = stringResource(id = R.string.title_failure)) },
            text = { Text(text = stringResource(id = R.string.label_project_added_failure)) },
            confirmButton = {
                Button(
                    onClick = {},
                ) {
                    Text(text = stringResource(id = R.string.button_ok))
                }
            }
        )
    }
}


@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
@Composable
private fun LM_AddCounterPagePreview() {
    val context = LocalContext.current
    JKCTheme {
        Surface {
            AddCounterPage(
                onBackButtonPressed = {},
                onAddButtonPressed = {},
                viewModel = AddCounterPageViewModel(
                    appRepository = AppRepository(
                        projectDao = AppDatabase.getDatabase(context).projectDao,
                        counterDao = AppDatabase.getDatabase(context).counterDao,
                        partDao = AppDatabase.getDatabase(context).partDao,
                    ),
                    owningPartId = 1L,
                    owningPartName = "Part 1",
                )
            )
        }
    }
}

@Preview(name = "Dark Mode", showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DM_AddCounterPagePreview() {
    val context = LocalContext.current
    JKCTheme {
        Surface {
            AddCounterPage(
                onBackButtonPressed = {},
                onAddButtonPressed = {},
                viewModel = AddCounterPageViewModel(
                    appRepository = AppRepository(
                        projectDao = AppDatabase.getDatabase(context).projectDao,
                        counterDao = AppDatabase.getDatabase(context).counterDao,
                        partDao = AppDatabase.getDatabase(context).partDao,
                    ),
                    owningPartId = 1L,
                    owningPartName = "Part 1",
                )
            )
        }
    }
}