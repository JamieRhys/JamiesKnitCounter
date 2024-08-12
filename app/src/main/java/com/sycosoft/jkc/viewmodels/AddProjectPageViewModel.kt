package com.sycosoft.jkc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycosoft.jkc.database.entities.Part
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.database.result.DatabaseResultCode
import com.sycosoft.jkc.util.LoadingState
import com.sycosoft.jkc.util.ProjectType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddProjectPageViewModel(
    private val appRepository: AppRepository,
) : ViewModel() {
    private var _loadingState = MutableStateFlow(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState

    fun addProject(
        projectName: String,
        projectDescription: String,
        projectType: ProjectType,
    ) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            val result = appRepository.addFreshProject(
                Project(
                    name = projectName,
                    description = projectDescription,
                    type = projectType
                )
            )

            if(result.code == DatabaseResultCode.CreationSuccess) {
                _loadingState.value = LoadingState.Success
            } else {
                _loadingState.value = LoadingState.Failure
            }
        }
    }
}