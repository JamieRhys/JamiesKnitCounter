package com.sycosoft.jkc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycosoft.jkc.database.dao.ProjectDao
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.database.result.DatabaseResultCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddProjectPageViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    companion object {
        enum class Result {
            None,
            Success,
            Loading,
            Failure,
        }
    }

    private val _projectName = MutableStateFlow("")
    val projectName: StateFlow<String> = _projectName

    private val _result = MutableStateFlow(Result.None)
    val result: StateFlow<Result> = _result

    fun saveProject(
        projectName: String
    ) {
        viewModelScope.launch {
            _result.value = Result.Loading

            val project = appRepository.addProject(Project(projectName = projectName))

            if(project.code != DatabaseResultCode.CreationSuccess) {
                _result.value = Result.Failure
            }
            else {
                _result.value = Result.Success
            }
        }
    }
}