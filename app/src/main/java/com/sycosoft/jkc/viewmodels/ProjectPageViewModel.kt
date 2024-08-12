package com.sycosoft.jkc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.database.entities.Part
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.repository.AppRepository
import com.sycosoft.jkc.ui.pages.ProjectPage
import com.sycosoft.jkc.util.CounterType
import com.sycosoft.jkc.util.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** This class is responsible for providing the data for the [ProjectPage] to be able to display along with
 * providing a way to interface with the [AppRepository].
 *
 * @property appRepository The [AppRepository] instance to be used to interact with the database.
 * @property projectId The ID of the [Project] currently selected by the user.
 */
class ProjectPageViewModel(
    private val appRepository: AppRepository,
    private val projectId: Long,
): ViewModel() {

    // The Project object currently selected by the user. This is then exposed to the UI through the
    // public project property.
    private val _project: MutableStateFlow<Project?> = MutableStateFlow(null)
    val project: StateFlow<Project?> = _project

    // The Parts list for the Project. This is then exposed to the UI through the public parts property.
    private val _parts: MutableStateFlow<List<Part>> = MutableStateFlow(emptyList())
    val parts: StateFlow<List<Part>> = _parts

    // The active Part object for the Project. This is then exposed to the UI through the public activePart property.
    private val _activePart: MutableStateFlow<Part?> = MutableStateFlow(null)
    val activePart: StateFlow<Part?> = _activePart

    // A list of counters available for the currently selected Part object. This is then exposed to the UI through the public counters object.
    private val _counters: MutableStateFlow<MutableList<Counter>> = MutableStateFlow(mutableListOf())
    val counters: StateFlow<List<Counter>> = _counters

    // The loading state of the page currently. This is then exposed to the UI through the public loadingState property.
    private val _loadingState: MutableStateFlow<LoadingState> = MutableStateFlow(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState

    init {
        viewModelScope.launch {
            // Set the LoadingState to Loading so that the UI can show a loading indicator.
            _loadingState.value = LoadingState.Loading

            // Retrieve the project from the database using it's ID. Then update the private property which in
            // turn updates the public one. This will then trigger a recomposition of the UI.
            _project.value = appRepository.getProject(projectId)

            if(_project.value != null) {
                // If we were able to retrieve the project from the database, we can now retrieve the parts
                _parts.value = appRepository.getProjectParts(projectId)

                // There should always be at least one part for a given project. Check if it's not
                // empty before continuing.
                if(_parts.value.isNotEmpty()) {
                    // If it's not, we can then set the active part to the first part in the list.
                    _parts.value.forEach { part ->
                        if(part.isCurrent) {
                            _activePart.value = part
                        }
                    }
                } else {
                    // If the list is empty, it was not retrieved properly from the database. return
                    // a loading state of Failure so that we can notify the user.
                    _loadingState.value = LoadingState.Failure
                    return@launch
                }

                if(activePart.value != null) {
                    // If the active part is not null, we can then retrieve the counters for the part.
                    refreshCounters()
                }

                // As we've done everything we've needed to, we can then set the loading state to Idle
                // which will tell the UI to display the main content to the user using the data we've
                // retrieved.
                _loadingState.value = LoadingState.Idle
            } else {
                // If the project was not retrieved properly from the database, we can then set the
                // loading state to Failure which will trigger the UI to notify the user and return back
                // to the HomePage.
                _loadingState.value = LoadingState.Failure
                return@launch
            }
        }
    }

    /** Refreshes the counters list with the most up-to-date data for the active part.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    private suspend fun refreshCounters() {
        _counters.value = appRepository.getPartCounters(activePart.value!!.id).toMutableList()
    }

    /** Increments the counter at the specified index of the counters list.
     *
     * @property index The index of the counter to increment.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    fun incrementCounter(index: Int) {
        viewModelScope.launch {
            val updatedCounter = _counters.value[index].copy().apply { increment() }

            val updatedList = _counters.value.toMutableList().apply { this[index] = updatedCounter }

            _counters.value = updatedList

            appRepository.updateCounter(updatedCounter)

            if(updatedCounter.counterType == CounterType.Global) {
                _counters.value.forEach { counter ->
                    if(counter.isGloballyLinked && counter.counterType == CounterType.Normal) {
                        incrementCounter(counter)
                    }
                }
            }
        }
    }

    /** Increments the [Counter] passed to it.
     *
     * @property counter The [Counter] to increment.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    private fun incrementCounter(counter: Counter) {
        viewModelScope.launch {
            val index = _counters.value.indexOf(counter)

            if(index != -1) {
                incrementCounter(index)
            }
        }
    }

    /** Decrements the counter at the specified index of the counters list.
     *
     * @property index The index of the counter to decrement.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    fun decrementCounter(index: Int) {
        viewModelScope.launch {
            val updatedCounter = _counters.value[index].copy().apply { decrement() }

            val updatedList = _counters.value.toMutableList().apply { this[index] = updatedCounter }

            _counters.value = updatedList

            appRepository.updateCounter(updatedCounter)

            if(updatedCounter.counterType == CounterType.Global) {
                _counters.value.forEach { counter ->
                    if(counter.isGloballyLinked && counter.counterType == CounterType.Normal) {
                        decrementCounter(counter)
                    }
                }
            }
        }
    }

    /** Decrements the [Counter] passed to it.
     *
     * @property counter The [Counter] to decrement.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    private fun decrementCounter(counter: Counter) {
        viewModelScope.launch {
            val index = _counters.value.indexOf(counter)

            if(index != -1) {
                decrementCounter(index)
            }
        }
    }

    /** Takes the passed index of the counters list, searches the list for the counter at the index location
     * and then toggles the [Counter.isGloballyLinked] property. It then updates the list and the database with
     * the new value.
     *
     * @property index The index of the [Counter] within the counters list.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     *
     */
    fun toggleGloballyLinked(index: Int) {
        viewModelScope.launch {
            val updatedCounter = _counters.value[index].copy().apply { globallyLinked() }

            val updatedList = _counters.value.toMutableList().apply { this[index] = updatedCounter }

            _counters.value = updatedList

            appRepository.updateCounter(updatedCounter)
        }
    }
}