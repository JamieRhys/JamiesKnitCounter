package com.sycosoft.jkc.ui.sections.addprojectpage

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sycosoft.jkc.R
import com.sycosoft.jkc.util.ProjectType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionSection(
    projectName: String,
    onProjectNameChange: (String) -> Unit,
    projectDescription: String,
    onProjectDescriptionChange: (String) -> Unit,
    projectType: ProjectType,
    onProjectTypeChange: (ProjectType) -> Unit,
) {
    // Project name
        OutlinedTextField(
        modifier = Modifier.padding(5.dp),
        value = projectName,
        onValueChange = onProjectNameChange,
        label = { Text(text = stringResource(id = R.string.label_project_name)) },
        singleLine = true,
    )

    // Project description
    OutlinedTextField(
        modifier = Modifier.padding(5.dp),
        value = projectDescription,
        onValueChange = onProjectDescriptionChange,
        label = { Text(text = stringResource(id = R.string.label_project_description)) },
        singleLine = true,
    )

    // Project type
    Text(
        modifier = Modifier.padding(top = 5.dp),
        text = stringResource(id = R.string.label_project_type)
    )
    SingleChoiceSegmentedButtonRow(
    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
    ) {
        ProjectType.entries.forEach { option ->
            SegmentedButton(
                onClick = { onProjectTypeChange(option) },
                modifier = Modifier.weight(1f),
                selected = projectType == option,
                shape = SegmentedButtonDefaults.itemShape(index = option.ordinal, count = ProjectType.entries.size),
            ) {
                Text(option.toString())
            }
        }
    }
}