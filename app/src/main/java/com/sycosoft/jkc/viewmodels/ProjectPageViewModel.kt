package com.sycosoft.jkc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.util.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProjectPageViewModel(
    private val appRepository: AppRepository,
) : ViewModel() {
    private val LOG_TAG = ProjectPageViewModel::class.java.simpleName

    private val _counterList: MutableStateFlow<List<Counter>> = MutableStateFlow(emptyList())
    val counterList: StateFlow<List<Counter>> = _counterList

    private val _projectId: MutableStateFlow<Long> = MutableStateFlow(0L)
    val projectId: StateFlow<Long> = _projectId

    fun init(projectId: Long) {
        _projectId.value = projectId
        refreshCounters()
    }

    private fun refreshCounters() {
        // Implement loading state here.
        viewModelScope.launch {
            _counterList.value = appRepository.getProjectCounters(projectId.value)
        }
    }
}