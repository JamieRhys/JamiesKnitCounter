package com.sycosoft.jkc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.util.LoadingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddCounterPageViewModel(
    private val appRepository: AppRepository,
    private val owningPartId: Long,
    owningPartName: String,
) : ViewModel() {
    private var _owningPartName = MutableStateFlow(owningPartName)
    val owningPartName: StateFlow<String> = _owningPartName

    private var _loadingState = MutableStateFlow(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState

    fun addCounter(
        counterName: String,
        incrementCounterBy: String,
        isGloballyLinked: Boolean,
        resetRow: String,
        maxResets: String,
    ) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            delay(1000)

            _loadingState.value = LoadingState.Success
        }
    }
}