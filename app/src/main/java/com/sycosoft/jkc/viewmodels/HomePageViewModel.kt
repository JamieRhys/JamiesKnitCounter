package com.sycosoft.jkc.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.ui.views.HomePage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * The view model for the home page. This allows the [HomePage] to access and display different bits
 * of data to the user.
 */
class HomePageViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    private val LOG_TAG = HomePageViewModel::class.java.simpleName
    private val _projectList: MutableStateFlow<List<Project>> = MutableStateFlow(emptyList())
    val projectList: StateFlow<List<Project>> = _projectList

    fun refreshData() {
        viewModelScope.launch {
            _projectList.value = appRepository.getAllProjects()
        }
    }

    /**
     * Removes a project from the database.
     */
    fun removeProject(projectId: Long) {
        viewModelScope.launch {
            Log.i(LOG_TAG, "Removing project: $projectId")
            appRepository.removeProject(projectId = projectId, callback = { refreshData() })
        }
    }
}