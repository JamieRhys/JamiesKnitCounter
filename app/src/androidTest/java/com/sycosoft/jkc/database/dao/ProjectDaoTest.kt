package com.sycosoft.jkc.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.testdata.TestData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ProjectDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var projectDao: ProjectDao

// region Setup and Teardown
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()

        projectDao = database.projectDao
    }

    @After
    fun teardown() {
        database.close()
    }

// endregion
// region Tests
    // region Get All Projects

    @Test
    fun getAllProjects_returnsAllInsertedProjects() = runBlocking {
        projectDao.insertProject(TestData.validProject)
        val projects = projectDao.getAllProjects()

        assertEquals(1, projects.size)
        assertEquals(1L, projects[0].id)
        assertEquals(TestData.validProject.name, projects[0].name)
    }

    // endregion
    // region Get Project By Id

    @Test
    fun givenValidProjectId_getProjectById_returnsInsertedProject() = runBlocking {
        val projectId = projectDao.insertProject(TestData.validProject)
        val project = projectDao.getProjectById(projectId)

        assertNotNull(project)
        assertEquals(TestData.validProject.name, project?.name)
    }

    @Test
    fun givenInvalidProjectId_getProjectById_returnsNull() = runBlocking {
        val project = projectDao.getProjectById(1L)

        assertNull(project)
    }

    // endregion
    // region Insert Project

    @Test
    fun insertProject_returnsInsertedProjectId() = runBlocking {
        val projectId = projectDao.insertProject(TestData.validProject)

        assertNotNull(projectId)
        assertTrue(projectId > 0)
    }

    // endregion
    // region Delete Project By Id

    @Test
    fun givenValidProjectId_deleteProjectById_deletesProject() = runBlocking {
        val projectId = projectDao.insertProject(TestData.validProject)
        projectDao.deleteProjectById(projectId)

        assertNull(projectDao.getProjectById(projectId))
    }

    // endregion
    // region Delete All Projects

    @Test
    fun deleteAllProjects_deletesAllProjects() = runBlocking {
        projectDao.insertProject(TestData.validProject)
        projectDao.deleteAllProjects()

        assertTrue(projectDao.getAllProjects().isEmpty())
    }

    // endregion
// endregion
}