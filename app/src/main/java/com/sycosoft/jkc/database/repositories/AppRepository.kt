package com.sycosoft.jkc.database.repositories

import android.content.Context
import android.util.Log
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.dao.ProjectDao
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
    private val projectDao: ProjectDao = AppDatabase.getDatabase(context).projectDao

    private val _projectData: MutableStateFlow<List<Project>> = MutableStateFlow(emptyList())
    val projectData: StateFlow<List<Project>> = _projectData

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
     * Adds a project to the database.
     */
    suspend fun addProject(project: Project): DatabaseResult<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val savedProject = projectDao.addProject(project)
                refreshProjectData()

                DatabaseResult(
                    code = DatabaseResultCode.CreationSuccess,
                    entity = savedProject,
                )
            } catch(exception: Exception) {
                exception.printStackTrace()
                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                )
            }
        }
    }

    /**
     * Removes a project from the database and then triggers the callback function once done.
     */
    suspend fun removeProject(projectId: Long, callback: (() -> Unit)) {
        withContext(Dispatchers.IO) {
            try {
                projectDao.removeProject(projectId)
                refreshProjectData()
                callback()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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