package com.sycosoft.jkc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.util.LoadingState
import com.sycosoft.jkc.ui.pages.HomePage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** This class is responsible for providing an interface to the [HomePage] view so it can interact with
 * the [AppRepository] class. It also provides data for the UI to use and display.
 */
class HomePageViewModel(
    private val appRepository: AppRepository,
) : ViewModel() {
    // The project list object which will hold all projects available.
    private val _projectList: MutableStateFlow<List<Project>> = MutableStateFlow(emptyList())
    val projectList: StateFlow<List<Project>> = _projectList

    // Holds the current loading state of the HomePage.
    private val _loadingState: MutableStateFlow<LoadingState> = MutableStateFlow(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState

    init {
        viewModelScope.launch {
            _projectList.value = appRepository.getAllProjects()
        }
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading
            appRepository.deleteFullProject(id)

            delay(500)
            _projectList.value = appRepository.getAllProjects()
            _loadingState.value = LoadingState.Idle
        }
    }
}