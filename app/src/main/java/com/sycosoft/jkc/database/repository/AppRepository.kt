package com.sycosoft.jkc.database.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import com.sycosoft.jkc.database.dao.CounterDao
import com.sycosoft.jkc.database.dao.PartDao
import com.sycosoft.jkc.database.dao.ProjectDao
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.database.entities.Part
import com.sycosoft.jkc.database.result.DatabaseResult
import com.sycosoft.jkc.database.result.DatabaseResultCode
import com.sycosoft.jkc.util.CounterType
import com.sycosoft.jkc.util.exceptions.AlreadyExistsException
import com.sycosoft.jkc.util.exceptions.BlankNameException
import com.sycosoft.jkc.util.exceptions.CounterDoesNotExistException
import com.sycosoft.jkc.util.exceptions.CounterNotUpdatedException
import com.sycosoft.jkc.util.exceptions.DoesNotExistException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** This class is responsible for giving an agnostic interface for the app to communicate with the
 * database. Allowing the app to not care about where the database is coming from.
 *
 * @author Jamie-Rhys Edwards
 * @since v0.0.1
 */
class AppRepository(
    private val projectDao: ProjectDao,
    private val partDao: PartDao,
    private val counterDao: CounterDao,
) {
    private val logTag = AppRepository::class.java.simpleName

    companion object {
        class ExceptionMessages {
            companion object {
                const val COUNTER_ALREADY_EXISTS: String = "Counter already exists in database. Counter ID is not unique."
                const val COUNTER_DOES_NOT_EXIST: String = "Counter does not exist with given ID."
                const val COUNTER_ID_INVALID: String = "Counter should have a valid ID."
                const val COUNTER_NAME_BLANK: String = "Counter name cannot be blank."
                const val COUNTER_NOT_USER_DELETABLE: String = "Counter is either a Global or Stitch counter. It cannot be deleted."
                const val COUNTER_ZERO_OR_NEG_PART_ID: String = "Counter must belong to a part. Part ID cannot be negative or zero."

                const val OWNING_PROJECT_ID_INVALID: String = "Part must belong to a project. Project ID cannot be negative or zero."
                const val OWNING_PROJECT_DOES_NOT_EXIST: String = "Part must belong to a project. Project does not exist in database."
                const val PART_NAME_BLANK: String = "Part name cannot be blank."
                const val PART_ALREADY_EXISTS: String = "Part already exists in database. Part ID is not unique."
                const val PART_DOES_NOT_EXIST: String = "Part does not exist with given ID."
                const val PART_ZERO_OR_NEG_ID: String = "Part ID cannot be negative or zero."

                const val PROJECT_ALREADY_EXISTS: String = "Project already exists in database. Project ID is not unique."
                const val PROJECT_DOES_NOT_EXIST: String = "Project does not exist with given ID."
                const val PROJECT_NAME_BLANK: String = "Project name cannot be blank."
                const val PROJECT_ZERO_OR_NEG_ID: String = "Part ID cannot be negative or zero."
            }
        }
    }

// region Counter

    /** Adds a counter to the database.
     *
     * @property counter The [Counter] object to be added to the database.
     * @return A [DatabaseResult] which will notify the caller if the operation was successful or not.
     *
     * @see [DatabaseResultCode] for all possible results for an operation.
     * @see [deleteCounter] to delete a counter from the database.
     * @see [getCounter] to get a counter from the database.
     * @see [updateCounter] to update a counter in the database.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    suspend fun addCounter(counter: Counter): DatabaseResult<Long> {
        return withContext(Dispatchers.IO) {
            try {
                // Check if the counter name is blank or empty.
                if(counter.name.isBlank() || counter.name.isEmpty()) {
                    counter.name = "Blank name"
                    throw BlankNameException(ExceptionMessages.COUNTER_NAME_BLANK)
                }
                // Check if the ID for the counter is unique if above 0L.
                if(counter.id != 0L) {
                    if(counterDao.counterExists(counter.id)) {
                        throw AlreadyExistsException(ExceptionMessages.COUNTER_ALREADY_EXISTS)
                    }
                }
                // Check to see if the owning Part ID is valid.
                if(counter.owningPartId <= 0L) {
                    throw DoesNotExistException(ExceptionMessages.COUNTER_ZERO_OR_NEG_PART_ID)
                }

                // All going well, we should be able to pass on a success code and the
                // entity of the created counter (in this case, it's location ID).
                DatabaseResult(
                    code = DatabaseResultCode.CreationSuccess,
                    entity = counterDao.insertCounter(counter),
                )
            } catch(e: AlreadyExistsException) {
                val message = "Could not add counter '${counter.name}'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: BlankNameException) {
                val message = "Could not add counter \'${counter.name}\'. Reason: ${e.message}"

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: DoesNotExistException) {
                val message = "Could not add counter '${counter.name}'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: SQLiteException) {
                val message = "Could not add counter '${counter.name}'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: IllegalArgumentException) {
                val message = "Could not add counter '${counter.name}'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: Exception) {
                val message = "Could not add counter '${counter.name}'. New Error: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            }
        }
    }

    /** Deletes a counter from the database.
     *
     * @property id The ID of the [Counter] in the database. If it exists, it is then checked if it can be deleted and if so, the operation is performed.
     * @return A [DatabaseResult] object that holds the outcome of the operation.
     *
     * @see [DatabaseResultCode] for a list of outcome codes.
     * @see [addCounter] to add a counter to the database.
     * @see [getCounter] to get a counter from the database.
     * @see [updateCounter] to update a counter in the database.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    suspend fun deleteCounter(id: Long): DatabaseResult<Int> {
        return withContext(Dispatchers.IO) {
            try {
                // Check if the ID is valid. If not, throw and exception.
                if(id <= 0L) {
                    throw DoesNotExistException(ExceptionMessages.COUNTER_ID_INVALID)
                }

                // Check to make sure that the counter does exist in the database. If not, throw
                // an exception.
                if(!counterDao.counterExists(id)) {
                    throw DoesNotExistException(ExceptionMessages.COUNTER_DOES_NOT_EXIST)
                }

                // All going well, we should reach this. Now we can try to delete the counter. Any
                // problems and an exception will be raised by the database.
                DatabaseResult(
                    code = DatabaseResultCode.DeletionSuccess,
                    entity = counterDao.deleteCounterById(id),
                )
            } catch(e: DoesNotExistException) {
                val message = "Could not delete counter using ID '$id'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.DeletionFailure,
                    errorMessage = message,
                )
            } catch(e: Exception) {
                val message = "Could not delete counter using ID '$id'. New Error: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.DeletionFailure,
                    errorMessage = message
                )
            }
        }
    }

    /** Gets a counter from the database.
     *
     * @property id The ID of the [Counter] to get from the database.
     * @return A [DatabaseResult] of whether the database operation was successful or not.
     *
     * @see [DatabaseResultCode] for a list of possible outcomes.
     * @see [addCounter] to add a [Counter] to the database.
     * @see [deleteCounter] to delete a [Counter] from the database.
     * @see [updateCounter] to update a [Counter] within the database.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    suspend fun getCounter(id: Long): DatabaseResult<Counter> {
        return withContext(Dispatchers.IO) {
            try {
                // Check to see if the ID is valid.
                if(id <= 0L) {
                    throw IllegalArgumentException(ExceptionMessages.COUNTER_ID_INVALID)
                }

                // Check to see if the counter exists within the database.
                if(!counterDao.counterExists(id)) {
                    throw CounterDoesNotExistException()
                }

                // If we have made it this far, we should be able to provide a success code to the
                // caller along with the found counter as the entity.
                DatabaseResult(
                    code = DatabaseResultCode.FetchSuccess,
                    entity = counterDao.getCounterById(id),
                )
            } catch(e: CounterDoesNotExistException) {
                val message = "Could not get counter using ID: '$id'. Reason: ${e.message}"

                DatabaseResult(
                    code = DatabaseResultCode.FetchFailure,
                    errorMessage = message,
                )
            } catch(e: IllegalArgumentException) {
                val message = "Could not get counter using ID: '$id'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.FetchFailure,
                    errorMessage = message,
                )
            } catch(e: Exception) {
                val message = "Could not get counter using ID: '$id'. New Error: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.FetchFailure,
                    errorMessage = message,
                )
            }
        }
    }

    /** Updates a counter in the database.
     *
     * @property counter The [Counter] to be updated in the database.
     * @return A [DatabaseResult] object that holds the outcome of the operation.
     *
     * @see [DatabaseResultCode] for a list of operation outcomes.
     * @see [addCounter] to add a [Counter] to the database.
     * @see [deleteCounter] to delete a [Counter] from the database.
     * @see [getCounter] to get a [Counter] from the database.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    suspend fun updateCounter(counter: Counter): DatabaseResult<Int> {
        return withContext(Dispatchers.IO) {
            try {
                // As we now know that the counter exists within the database, let's get the original
                // version from the database so we can then do some checks in a moment.
                // If you check the function, you'll also see that it checks to make sure the counter
                // exists in the database.
                val originalCounter = getCounter(counter.id)

                if(originalCounter.code != DatabaseResultCode.FetchSuccess) {
                    // Rethrowing the exception as we need to notify the caller of the issue we've
                    // faced.
                    throw CounterDoesNotExistException()
                }

                // Let's now do our checks to see if anything has changed. First though, we need to
                // define a boolean to let us know if anything has changed.
                var hasChanged = false

                // Now let's do the checks
                // Name
                if(counter.name != originalCounter.entity?.name) {
                    hasChanged = true
                }

                // Value
                if(counter.value != originalCounter.entity?.value) {
                    hasChanged = true
                }

                // Is Globally Linked.
                if(counter.isGloballyLinked != originalCounter.entity?.isGloballyLinked) {
                    hasChanged = true
                }

                // Reset row
                if(counter.resetRow != originalCounter.entity?.resetRow) {
                    hasChanged = true
                }

                // Max Resets
                if(counter.maxResets != originalCounter.entity?.maxResets) {
                    hasChanged = true
                }

                // If hasChanged is still marked as false, we know that nothing has changed. We can
                // let the caller know this too.
                if(!hasChanged) {
                    throw CounterNotUpdatedException()
                }

                DatabaseResult(
                    code = DatabaseResultCode.UpdateSuccess,
                    entity = counterDao.updateCounter(counter)
                )
            } catch(e: CounterDoesNotExistException) {
                val message = "Could not update counter: '${counter.name}'. Reason: (GetCounter) ${e.message}"

                Log.d(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.UpdateFailure,
                    errorMessage = message,
                )
            } catch(e: CounterNotUpdatedException) {
                val message = "Could not update counter: '${counter.name}'. Reason: ${e.message}"

                Log.d(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.UpdateFailure,
                    errorMessage = message,
                )
            } catch(e: Exception) {
                val message = "Could not update counter: '${counter.name}'. New Error: ${e.message}"

                DatabaseResult(
                    code = DatabaseResultCode.UpdateFailure,
                    errorMessage = message,
                )
            }
        }
    }

// endregion
// region Part

    /** Adds a part to the database.
     *
     * @property part The [Part] object to be added to the database.
     * @return A [DatabaseResult] which will notify the caller if the operation was successful or not.
     *
     * @see [DatabaseResultCode] for all possible results for an operation.
     * @see [deletePart] to delete a part from the database.
     * @see [getPart] to get a part from the database.
     * @see [updatePart] to update a part in the database.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    suspend fun addPart(part: Part): DatabaseResult<Long> {
        return withContext(Dispatchers.IO) {
            try {
                // Let's check to make sure that the part iD is unique.
                if(part.id != 0L) {
                    if(doesPartExist(part.id)) {
                        throw AlreadyExistsException(ExceptionMessages.PART_ALREADY_EXISTS)
                    }
                }

                // Let's check to make sure that the part name is not blank.
                if(part.name.isBlank()) {
                    part.name = "Blank name"
                    throw BlankNameException(ExceptionMessages.PART_NAME_BLANK)
                }

                // Let's now check to make sure the owning project ID provided to us is valid.
                if(part.owningProjectId <= 0L) {
                    throw DoesNotExistException(ExceptionMessages.OWNING_PROJECT_ID_INVALID)
                }

                DatabaseResult(
                    code = DatabaseResultCode.CreationSuccess,
                    entity = partDao.insertPart(part),
                )
            } catch(e: AlreadyExistsException) {
                val message = "Could not add part '${part.name}'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: BlankNameException) {
                val message = "Could not add part '${part.name}'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: DoesNotExistException) {
                val message = "Could not add part '${part.name}'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: Exception) {
                val message = "Could not add part '${part.name}. New Error: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            }
        }
    }

    /** Deletes a part from the database.
     *
     * @property id The ID of the [Part] in the database. If it exists, it is then checked if it can be deleted and if so, the operation is performed.
     * @return A [DatabaseResult] object that holds the outcome of the operation.
     *
     * @see [DatabaseResultCode] for a list of outcome codes.
     * @see [addCounter] to add a counter to the database.
     * @see [getCounter] to get a counter from the database.
     * @see [updateCounter] to update a counter in the database.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    suspend fun deletePart(id: Long): DatabaseResult<Int> {
        return withContext(Dispatchers.IO) {
            try {
                // Check to see if the ID is negative or zero. If it is, throw an exception as we
                // cannot use it. This is because inherently it cannot exist in the database.
                if(id <= 0L) {
                    throw DoesNotExistException(ExceptionMessages.PART_ZERO_OR_NEG_ID)
                }

                // Check to see if the part exists in the database. If it doesn't, throw an exception.
                if(!partDao.partExists(id)) {
                    throw DoesNotExistException(ExceptionMessages.PART_DOES_NOT_EXIST)
                }

                // If we get this far, it's safe to use the ID to delete the part in question. However,
                // if for any reason an exception is thrown, we should notify the caller of that issue.
                // It should be caught by one of the catches below.
                DatabaseResult(
                    code = DatabaseResultCode.DeletionSuccess,
                    entity = partDao.deletePartById(id),
                )
            } catch(e: DoesNotExistException) {
                val message = "Could not delete part using ID '$id'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.DeletionFailure,
                    errorMessage = message,
                )
            } catch(e: Exception) {
                val message = "Could not delete part using ID '$id'. New Error: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.DeletionFailure,
                    errorMessage = message,
                )
            }
        }
    }

    private fun doesPartExist(id: Long): Boolean = partDao.partExists(id)

// endregion
// region Project

    /** Adds a project to the database.
     *
     * @property project The [Project] object to be added to the database.
     * @return A [DatabaseResult] which will notify the caller if the operation was successful or not.
     *
     * @see [DatabaseResultCode] for all possible results for an operation.
     * @see [deleteProject] to delete a project from the database.
     * @see [getProject] to get a project from the database.
     * @see [updateProject] to update a project in the database.
     *
     * @author Jamie-Rhys Edwards
     * @since v0.0.1
     */
    suspend fun addProject(project: Project): DatabaseResult<Long> {
        return withContext(Dispatchers.IO) {
            try {
                // Check to make sure that the project name is not blank.
                if(project.name.isBlank()) {
                    project.name = "Blank name"
                    throw BlankNameException(ExceptionMessages.PROJECT_NAME_BLANK)
                }

                // Check to see if the ID is zero.
                if(project.id != 0L) {
                    // If it's not, check to make sure it's unique.
                    if(doesProjectExist(project.id)) {
                        throw AlreadyExistsException(ExceptionMessages.PROJECT_ALREADY_EXISTS)
                    }
                }

                DatabaseResult(
                    code = DatabaseResultCode.CreationSuccess,
                    entity = projectDao.insertProject(project),
                )
            } catch(e: BlankNameException) {
                val message = "Could not add project '${project.name}'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: AlreadyExistsException) {
                val message = "Could not add project '${project.name}'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            } catch(e: Exception) {
                val message = "Could not add project '${project.name}'. New Error: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    errorMessage = message,
                )
            }
        }
    }

    suspend fun deleteProject(id: Long): DatabaseResult<Int> {
        return withContext(Dispatchers.IO) {
            try {
                // Check to see if the ID is negative or zero. If it is, throw an exception as we
                // cannot use it. This is because inherently it cannot exist in the database.
                if(id <= 0L) {
                    throw DoesNotExistException(ExceptionMessages.PROJECT_ZERO_OR_NEG_ID)
                }

                // Check to see if the part exists in the database. If it doesn't, throw an exception.
                if(!projectDao.projectExists(id)) {
                    throw DoesNotExistException(ExceptionMessages.PROJECT_DOES_NOT_EXIST)
                }

                // If we get this far, it's safe to use the ID to delete the part in question. However,
                // if for any reason an exception is thrown, we should notify the caller of that issue.
                // It should be caught by one of the catches below.
                DatabaseResult(
                    code = DatabaseResultCode.DeletionSuccess,
                    entity = projectDao.deleteProjectById(id),
                )
            } catch(e: DoesNotExistException) {
                val message = "Could not delete part using ID '$id'. Reason: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.DeletionFailure,
                    errorMessage = message,
                )
            } catch(e: Exception) {
                val message = "Could not delete part using ID '$id'. New Error: ${e.message}"

                Log.e(logTag, message)

                DatabaseResult(
                    code = DatabaseResultCode.DeletionFailure,
                    errorMessage = message,
                )
            }
        }
    }

    suspend fun getProject(id: Long) = projectDao.getProjectById(id)

    /** Gets all projects from the database. If none exist, returns an empty list. */
    suspend fun getAllProjects(): List<Project> = projectDao.getAllProjects()

    private suspend fun doesProjectExist(id: Long): Boolean = projectDao.projectExists(id)

// endregion
// region Add Fresh Project

    suspend fun addFreshProject(project: Project): DatabaseResult<Long> {
        return withContext(Dispatchers.IO) {
            if(project.name.isBlank()) {
                val size = getAllProjects().size + 1
                project.name = "Project $size"
            }

            // First up, let's try to add the project
            val savedProject = addProject(project)

            // Now we just need to make sure that the project was added successfully.
            if(savedProject.code != DatabaseResultCode.CreationSuccess) {
                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    entity = null,
                    errorMessage = savedProject.errorMessage
                )
            }

            // Now we should try to add a new part for the project.
            val savedPart = addPart(
                Part(
                    name = "Part 1",
                    isCurrent = true,
                    owningProjectId = savedProject.entity!!
                )
            )

            // And check to make sure this was added successfully.
            if(savedPart.code != DatabaseResultCode.CreationSuccess) {
                // As it was not created successfully, we should delete the project we just created
                // to keep the database clean. Then we need to notify the caller that we were not
                // successful and why.
                deleteProject(savedProject.entity)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    entity = null,
                    errorMessage = savedPart.errorMessage
                )
            }

            // Now we've made it this far, we should try and make both the global and stitch counters.
            val globalCounter = addCounter(
                Counter(
                    name = "Global",
                    owningPartId = savedPart.entity!!,
                    counterType = CounterType.Global,
                )
            )

            val stitchCounter = addCounter(
                Counter(
                    name = "Stitch",
                    owningPartId = savedPart.entity,
                    counterType = CounterType.Stitch,
                    isGloballyLinked = false,
                )
            )

            val testCounter1 = addCounter(
                Counter(
                    name = "Increment With Global",
                    owningPartId = savedPart.entity,
                    counterType = CounterType.Normal,
                )
            )

            val testCounter2 = addCounter(
                Counter(
                    name = "Don't Increment With Global",
                    owningPartId = savedPart.entity,
                    counterType = CounterType.Normal,
                    isGloballyLinked = false,
                )
            )

            // Let's now check to make sure the two counters have been made successfully.
            if(globalCounter.code != DatabaseResultCode.CreationSuccess || stitchCounter.code != DatabaseResultCode.CreationSuccess) {
                // As it was not created successfully, we should delete the project and part we just created
                // to keep the database clean. Then we need to notify the caller that we were not
                // successful and why.
                deleteProject(savedProject.entity)
                deletePart(savedPart.entity)

                DatabaseResult(
                    code = DatabaseResultCode.CreationFailure,
                    entity = null,
                    errorMessage = "Global or Stitch counters were not created successfully. Please see log for more information."
                )
            }

            DatabaseResult(
                code = DatabaseResultCode.CreationSuccess,
                entity = savedProject.entity,
            )
        }
    }

    suspend fun deleteFullProject(id: Long) {
        return withContext(Dispatchers.IO) {
            val parts = getProjectParts(id)

            if(parts.isNotEmpty()) {
                parts.forEach { part ->
                    val counters = getPartCounters(part.id)

                    if(counters.isNotEmpty()) {
                        counters.forEach { counter ->
                            deleteCounter(counter.id)
                        }

                    }

                    deletePart(part.id)
                }
            }

            deleteProject(id)
        }
    }

    suspend fun getProjectParts(id: Long): List<Part> = partDao.getProjectParts(id)

    suspend fun getPartCounters(id: Long): List<Counter> = counterDao.getPartCounters(id)

// endregion
}