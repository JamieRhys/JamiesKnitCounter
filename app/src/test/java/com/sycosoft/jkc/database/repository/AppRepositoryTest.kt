package com.sycosoft.jkc.database.repository

import com.sycosoft.jkc.database.dao.CounterDao
import com.sycosoft.jkc.database.dao.PartDao
import com.sycosoft.jkc.database.dao.ProjectDao
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.database.entities.Part
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.result.DatabaseResultCode
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class AppRepositoryTest {
    private lateinit var projectDao: ProjectDao
    private lateinit var partDao: PartDao
    private lateinit var counterDao: CounterDao
    private lateinit var appRepository: AppRepository

// region Setup and Teardown
    @Before
    fun setup() {
        projectDao = mock()
        partDao = mock()
        counterDao = mock()
        appRepository = AppRepository(projectDao, partDao, counterDao)
    }

// endregion
// region Tests

    // region Counter

        // region Add

    @Test
    fun `When adding a counter, addCounter should add the counter to the database and return a success code with entity`(): Unit = runBlocking {
        val counter = TestData.counter.copy()

        `when`(counterDao.insertCounter(counter)).thenReturn(counter.id)

        val result = appRepository.addCounter(counter)

        assertNull(result.errorMessage)
        assertNotNull(result.entity)
        assertEquals(DatabaseResultCode.CreationSuccess, result.code)
        assertEquals(counter.id, result.entity)

        verify(counterDao).insertCounter(counter)
    }

    @Test
    fun `When given blank counter name, addCounter() should return failure code and error message`(): Unit = runBlocking {
        val counter = TestData.counter.copy(name = " ")
        val exceptionMessage = "Could not add counter 'Blank name'. Reason: ${AppRepository.Companion.ExceptionMessages.COUNTER_NAME_BLANK}"

        val result = appRepository.addCounter(counter)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(counterDao, never()).insertCounter(counter)
    }

    @Test
    fun `When given counter with non-unique ID, addCounter() should return failure code and error message`(): Unit = runBlocking {
        val counter = TestData.counter.copy()
        val exceptionMessage = "Could not add counter '${counter.name}'. Reason: ${AppRepository.Companion.ExceptionMessages.COUNTER_ALREADY_EXISTS}"

        `when`(counterDao.counterExists(counter.id)).thenReturn(true)

        val result = appRepository.addCounter(counter)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(counterDao).counterExists(counter.id)
        verify(counterDao, never()).insertCounter(counter)
    }

    @Test
    fun `When given part ID is negative, addCounter() should return failure code and error message`(): Unit = runBlocking {
        val counter = TestData.counter.copy(owningPartId = -1L)
        val exceptionMessage = "Could not add counter '${counter.name}'. Reason: ${AppRepository.Companion.ExceptionMessages.COUNTER_ZERO_OR_NEG_PART_ID}"

        val result = appRepository.addCounter(counter)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(counterDao).counterExists(counter.id)
        verify(counterDao, never()).insertCounter(counter)
    }

    @Test
    fun `When given part ID is zero, addCounter() should return failure code and error message`(): Unit = runBlocking {
        val counter = TestData.counter.copy(owningPartId = 0L)
        val exceptionMessage = "Could not add counter '${counter.name}'. Reason: ${AppRepository.Companion.ExceptionMessages.COUNTER_ZERO_OR_NEG_PART_ID}"

        val result = appRepository.addCounter(counter)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(counterDao).counterExists(counter.id)
        verify(counterDao, never()).insertCounter(counter)
    }

        // endregion

        // region Delete

    @Test
    fun `When deleting a counter, deleteCounter() should delete the counter from the database and return success code`(): Unit = runBlocking {
        val counter = TestData.counter.copy()

        `when`(counterDao.deleteCounterById(counter.id)).thenReturn(1)

        val result = appRepository.deleteCounter(counter.id)

        assertNull(result.errorMessage)
        assertNotNull(result.entity)
        assertEquals(DatabaseResultCode.DeletionSuccess, result.code)
        assertEquals(1, result.entity)

        verify(counterDao).deleteCounterById(counter.id)
    }

    @Test
    fun `When given negative counter ID, deleteCounter() should return failure code and error message`(): Unit = runBlocking {
        val counter = TestData.counter.copy(id = -1L)
        val exceptionMessage = "Could not delete counter using ID '${counter.id}'. Reason: ${AppRepository.Companion.ExceptionMessages.COUNTER_ID_INVALID}"

        val result = appRepository.deleteCounter(counter.id)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.DeletionFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(counterDao, never()).deleteCounterById(counter.id)
    }

    @Test
    fun `When given a zero counter ID, deleteCounter() should return failure code and error message`(): Unit = runBlocking {
        val counter = TestData.counter.copy(id = 0L)
        val exceptionMessage = "Could not delete counter using ID '${counter.id}'. Reason: ${AppRepository.Companion.ExceptionMessages.COUNTER_ID_INVALID}"

        val result = appRepository.deleteCounter(counter.id)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.DeletionFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(counterDao, never()).deleteCounterById(counter.id)
    }

    @Test
    fun `When given a non-existent counter ID, deleteCounter() should return failure code and error message`(): Unit = runBlocking {
        val counter = TestData.counter.copy()
        val exceptionMessage = "Could not delete counter using ID '${counter.id}'. Reason: ${AppRepository.Companion.ExceptionMessages.COUNTER_DOES_NOT_EXIST}"

        `when`(counterDao.counterExists(counter.id)).thenReturn(false)

        val result = appRepository.deleteCounter(counter.id)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.DeletionFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(counterDao, never()).deleteCounterById(counter.id)
        verify(counterDao).counterExists(counter.id)
    }

        // endregion

        // region Get



        // endregion

        // region Update



        // endregion

    // endregion

    // region Part

        // region Add

    @Test
    fun `When adding a part, addPart() should add the part to the database and return success code with entity`(): Unit = runBlocking {
        val part = TestData.part.copy()

        `when`(partDao.insertPart(part)).thenReturn(part.id)
        `when`(projectDao.projectExists(part.owningProjectId)).thenReturn(true)

        val result = appRepository.addPart(part)

        assertNull(result.errorMessage)
        assertNotNull(result.entity)
        assertEquals(DatabaseResultCode.CreationSuccess, result.code)
        assertEquals(part.id, result.entity)

        verify(partDao).insertPart(part)
    }

    @Test
    fun `When given blank part name, addPart() should return failure code and error message`(): Unit = runBlocking {
        val part = TestData.part.copy(name = " ")
        val exceptionMessage = "Could not add part 'Blank name'. Reason: ${AppRepository.Companion.ExceptionMessages.PART_NAME_BLANK}"

        val result = appRepository.addPart(part)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(partDao, never()).insertPart(part)
    }

    @Test
    fun `when given part with non-unique ID, addPart() should return failure code and error message`(): Unit = runBlocking {
        val part = TestData.part.copy()
        val exceptionMessage = "Could not add part '${part.name}'. Reason: ${AppRepository.Companion.ExceptionMessages.PART_ALREADY_EXISTS}"

        `when`(partDao.partExists(part.id)).thenReturn(true)

        val result = appRepository.addPart(part)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(partDao, never()).insertPart(part)
        verify(partDao).partExists(part.id)
    }

    @Test
    fun `When given project ID that is negative, addPart() should return failure code and error message`(): Unit = runBlocking {
        val part = TestData.part.copy(owningProjectId = -1L)
        val exceptionMessage = "Could not add part '${part.name}'. Reason: ${AppRepository.Companion.ExceptionMessages.OWNING_PROJECT_ID_INVALID}"

        `when`(projectDao.projectExists(part.id)).thenReturn(false)

        val result = appRepository.addPart(part)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(partDao).partExists(part.id)
        verify(partDao, never()).insertPart(part)
    }

    @Test
    fun `When given project ID that is zero, addPart() should return failure code and error message`(): Unit = runBlocking {
        val part = TestData.part.copy(owningProjectId = 0L)
        val exceptionMessage = "Could not add part '${part.name}'. Reason: ${AppRepository.Companion.ExceptionMessages.OWNING_PROJECT_ID_INVALID}"

        `when`(projectDao.projectExists(part.id)).thenReturn(false)

        val result = appRepository.addPart(part)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(partDao).partExists(part.id)
        verify(partDao, never()).insertPart(part)
    }

        // endregion

        // region Delete

    @Test
    fun `When deleting a part, deletePart() should delete part in database and return success code`(): Unit = runBlocking {
        val part = TestData.part.copy()

        `when`(partDao.deletePartById(part.id)).thenReturn(1)

        val result = appRepository.deletePart(part.id)

        assertNull(result.errorMessage)
        assertNotNull(result.entity)
        assertEquals(DatabaseResultCode.DeletionSuccess, result.code)
        assertEquals(1, result.entity)

        verify(partDao).deletePartById(part.id)
    }

    @Test
    fun `When giving negative part ID, deletePart() should return failure code and error message`(): Unit = runBlocking {
        val part = TestData.part.copy(id = -1L)
        val exceptionMessage = "Could not delete part using ID '${part.id}'. Reason: ${AppRepository.Companion.ExceptionMessages.PART_ZERO_OR_NEG_ID}"

        val result = appRepository.deletePart(part.id)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.DeletionFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(partDao, never()).deletePartById(part.id)
    }

    @Test
    fun `When giving zero part ID, deletePart() should return failure code and error message`(): Unit = runBlocking {
        val part = TestData.part.copy(id = 0L)
        val exceptionMessage = "Could not delete part using ID '${part.id}'. Reason: ${AppRepository.Companion.ExceptionMessages.PART_ZERO_OR_NEG_ID}"

        val result = appRepository.deletePart(part.id)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.DeletionFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(partDao, never()).deletePartById(part.id)
    }

    @Test
    fun `When giving non-existent part ID, deletePart() should return failure code and error message`(): Unit = runBlocking {
        val part = TestData.part.copy()
        val exceptionMessage = "Could not delete part using ID '${part.id}'. Reason: ${AppRepository.Companion.ExceptionMessages.PART_DOES_NOT_EXIST}"

        `when`(partDao.partExists(part.id)).thenReturn(false)

        val result = appRepository.deletePart(part.id)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.DeletionFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(partDao).partExists(part.id)
        verify(partDao, never()).deletePartById(part.id)
    }

        // endregion

        // region Get



        // endregion

        // region Update



        // endregion

    // endregion

    // region Project

        // region Add

    @Test
    fun `When adding a project, addProject() should add the project and return success code with entity`(): Unit = runBlocking {
        val project = TestData.project.copy()

        `when`(projectDao.insertProject(project)).thenReturn(project.id)
        `when`(projectDao.projectExists(project.id)).thenReturn(false)

        val result = appRepository.addProject(project)

        assertNull(result.errorMessage)
        assertNotNull(result.entity)
        assertEquals(DatabaseResultCode.CreationSuccess, result.code)
        assertEquals(project.id, result.entity)

        verify(projectDao).insertProject(project)
    }

    @Test
    fun `When given non-unique ID, addProject() should return failure code and error message`(): Unit = runBlocking {
        val project = TestData.project.copy()
        val exceptionMessage = "Could not add project '${project.name}'. Reason: ${AppRepository.Companion.ExceptionMessages.PROJECT_ALREADY_EXISTS}"

        `when`(projectDao.projectExists(project.id)).thenReturn(true)

        val result = appRepository.addProject(project)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(projectDao).projectExists(project.id)
        verify(projectDao, never()).insertProject(project)
    }

    @Test
    fun `When given blank project name, addProject() should return failure code and error message`(): Unit = runBlocking {
        val project = TestData.project.copy(name = " ")
        val exceptionMessage = "Could not add project 'Blank name'. Reason: ${AppRepository.Companion.ExceptionMessages.PROJECT_NAME_BLANK}"

        val result = appRepository.addProject(project)

        assertNull(result.entity)
        assertNotNull(result.errorMessage)
        assertEquals(DatabaseResultCode.CreationFailure, result.code)
        assertEquals(exceptionMessage, result.errorMessage)

        verify(projectDao, never()).insertProject(project)
        verify(projectDao, never()).projectExists(project.id)
    }

        // endregion

        // region Delete

    @Test
    fun `When given a valid project ID, deleteProject() should delete the project from the database and return success code`(): Unit = runBlocking {
        val project = TestData.project.copy()

        `when`(projectDao.projectExists(project.id)).thenReturn(true)
        `when`(projectDao.deleteProjectById(project.id)).thenReturn(1)

        val result = appRepository.deleteProject(project.id)

        assertNull(result.errorMessage)
        assertNotNull(result.entity)
        assertEquals(DatabaseResultCode.DeletionSuccess, result.code)
        assertEquals(1, result.entity)

        verify(projectDao).deleteProjectById(project.id)
    }

        // endregion

        // region Get



        // endregion

        // region Update



        // endregion

    // endregion

    // region Add Fresh Project

    @Test
    fun `When adding a fresh project, addFreshProject() should add the project and return success code with entity`(): Unit = runBlocking {
        val project = Project(name = "Test Project")

        // Project
        `when`(projectDao.insertProject(project)).thenReturn(TestData.project.id)

        val result = appRepository.addFreshProject(project)

        assertNull(result.errorMessage)
        assertNotNull(result.entity)
        assertEquals(DatabaseResultCode.CreationSuccess, result.code)
        assertEquals(TestData.project.id, result.entity)

        verify(projectDao).insertProject(project)
    }

    // endregion

// endregion
// region Test Data

    private class TestData {
        companion object {
            val counter = Counter(id = 1L, name = "Test Counter", owningPartId = 1L)
            val part = Part(id = 1L, name = "Test Part", owningProjectId = 1L)
            val project = Project(id = 1L, name = "Test Project")
        }
    }

// endregion
}