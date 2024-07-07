package com.sycosoft.jkc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.database.result.DatabaseResultCode
import com.sycosoft.jkc.util.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddProjectPageViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _projectName = MutableStateFlow("")
    val projectName: StateFlow<String> = _projectName

    private val _loadingState = MutableStateFlow(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState

    fun saveProject(
        projectName: String
    ) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading

            val project = appRepository.addProject(Project(projectName = projectName))

            if(project.code != DatabaseResultCode.CreationSuccess) {
                _loadingState.value = LoadingState.Failure
            }
            else {
                _loadingState.value = LoadingState.Success
            }
        }
    }
}