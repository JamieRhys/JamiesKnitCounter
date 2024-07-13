package com.sycosoft.jkc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.database.repositories.AppRepository
import com.sycosoft.jkc.database.result.DatabaseResultCode
import com.sycosoft.jkc.util.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddCounterPageViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    private val _loadingState = MutableStateFlow(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState

    // TODO: Test to be sure this works as expected. Run on device to make sure it switches between the two views.

    fun saveCounter(counter: Counter) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading

            val savedCounter = appRepository.addCounter(counter)

            if(savedCounter.code != DatabaseResultCode.CreationSuccess) {
                _loadingState.value = LoadingState.Failure
            }
            else {
                _loadingState.value = LoadingState.Success
            }
        }
    }
}