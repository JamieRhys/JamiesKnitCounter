package com.sycosoft.jkc.database.repositories

import android.content.Context
import android.util.Log
import androidx.compose.ui.res.stringResource
import com.sycosoft.jkc.R
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.dao.CounterDao
import com.sycosoft.jkc.database.dao.ProjectDao
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.result.DatabaseResult
import com.sycosoft.jkc.database.result.DatabaseResultCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The repository for the app. Holds all actions relating to accessing the database and
 * sending/retrieving information from it.
 */
class AppRepository(
    context: Context
) {
    private val LOG_TAG = AppRepository::class.java.simpleName
    private val projectDao: ProjectDao = AppDatabase.getDatabase(context).projectDao
    private val counterDao: CounterDao = AppDatabase.getDatabase(context).counterDao

    private val _projectData: MutableStateFlow<List<Project>> = MutableStateFlow(emptyList())
    val projectData: StateFlow<List<Project>> = _projectData

    private val _counterData: MutableStateFlow<List<Counter>> = MutableStateFlow(emptyList())
    val counterData: StateFlow<List<Counter>> = _counterData

    /**
     * Initialises the repository and then calls the callback function, once done.
     */
    fun init(callback: (() -> Unit)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = getAllProjects()

            if(data.isEmpty()) {
                Log.i("AppRepository", "Generating example project.")
            }
            else {
                _projectData.value = data
            }

            callback?.invoke()
        }
    }

    /**
     * Adds a project to the database. It also creates a global and stitch counter and links them to
     * the new project.
     *
     * @param project The project to be added.
     * @return The result of the operation as a [DatabaseResult].
     */
    suspend fun addProject(project: Project): DatabaseResult<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val savedProject = projectDao.addProject(project)
                refreshProjectData()

                val globalCounter = Counter(
                    isGlobal = true,
                    counterName = "Global",
                    showLink = false,
                    canReset = false,
                    canBeUserDeleted = false,
                    owningProjectId = savedProject,
                )

                val stitchCounter = Counter(
                    isStitch = true,
                    counterName = "Stitches",
                    showLink = false,
                    canReset = false,
                    canBeUserDeleted = false,
                    owningProjectId = savedProject,
                )

                counterDao.addCounter(globalCounter)
                counterDao.addCounter(stitchCounter)

                DatabaseResult(
                    code = DatabaseResultCode.CreationSuccess,
                    entity = savedProject,
                )
            } catch(exception: Exception) {
                exception.printStackTrace()
                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    message = exception.message
                )
            }
        }
    }

    suspend fun addCounter(counter: Counter): DatabaseResult<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val savedCounter = counterDao.addCounter(counter)

                getProjectCounters(counter.owningProjectId)

                DatabaseResult(
                    code = DatabaseResultCode.CreationSuccess,
                    entity = savedCounter,
                )
            } catch(exception: Exception) {
                exception.printStackTrace()
                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    message = exception.message
                )
            }
        }
    }

    /**
     * Removes a project from the database and then triggers the callback function once done.
     * It also removes all counters that are associated with the project from the database.
     */
    suspend fun removeProject(projectId: Long, callback: (() -> Unit)) {
        withContext(Dispatchers.IO) {
            try {
                projectDao.removeProject(projectId)

                refreshProjectData()

                val counters = counterDao.getProjectCounters(projectId)

                for(counter in counters) {
                    counterDao.removeCounter(counter.id)
                }

                callback()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getProjectCounters(projectId: Long): List<Counter> {
        return withContext(Dispatchers.IO) {
            counterDao.getProjectCounters(projectId)
        }
    }

    /**
     * Refreshes the project data.
     */
    fun refreshProjectData() {
        // Launch a new coroutine to avoid blocking the main thread.
        CoroutineScope(Dispatchers.IO).launch {
            // Grab the latest data from the database.
           _projectData.value = getAllProjects()
        }
    }

    suspend fun getProjectById(id: Long): Project? {
        return try {
            projectDao.getById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Gets all projects from the database.
     */
    suspend fun getAllProjects(): List<Project> =
        try {
            projectDao.getAllProjects()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
}