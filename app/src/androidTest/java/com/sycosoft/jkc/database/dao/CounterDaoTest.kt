package com.sycosoft.jkc.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.sycosoft.jkc.database.AppDatabase
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.testdata.TestData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CounterDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var projectDao: ProjectDao
    private lateinit var counterDao: CounterDao

// region Setup and Teardown
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()

        projectDao = database.projectDao
        counterDao = database.counterDao
    }

    @After
    fun teardown() {
        database.close()
    }

// endregion
// region Tests

    // region Get All Counters

    @Test
    fun getAllCounters_returnsAllInsertedCounters() = runBlocking {
        counterDao.insertCounter(Counter(name = "Test Counter", owningPartId = 1L))
        val counters = counterDao.getAllCounters()

        assertEquals(1, counters.size)
    }

    // endregion
    // region Get Project Counters

    @Test
    fun givenValidProjectId_getProjectCounters_returnsAllProjectCounters() = runBlocking {
        counterDao.insertCounter(TestData.validCounter)
        counterDao.insertCounter(Counter(name = "Irrelevant", owningPartId = 2L))

        val counters = counterDao.getPartCounters(1L)

        assertEquals(1, counters.size)
        assertEquals(TestData.validCounter.name, counters[0].name)
    }

    // endregion
    // region Insert Counter

    @Test
    fun givenValidCounter_insertCounter_returnsInsertedCounterId() = runBlocking {
        val projectId = projectDao.insertProject(TestData.validProject)
        val counterId = counterDao.insertCounter(Counter(name = TestData.validCounter.name, owningPartId = projectId))

        assertNotNull(counterId)
        assertEquals(1L, counterId)
    }

    // endregion
    // region Delete Counter By Id

    @Test
    fun givenValidCounterId_deleteCounterById_deletesCounter() = runBlocking {
        val projectId = projectDao.insertProject(TestData.validProject)
        val counterId = counterDao.insertCounter(Counter(name = TestData.validCounter.name, owningPartId = projectId))

        counterDao.deleteCounterById(counterId)

        assertNull(counterDao.getCounterById(counterId))
    }

    // endregion
    // region Get Counter By Id

    @Test
    fun givenValidCounterId_getCounterById_returnsCounter() = runBlocking {
        val projectId = projectDao.insertProject(TestData.validProject)
        val counterId = counterDao.insertCounter(Counter(name = TestData.validCounter.name, owningPartId = projectId))
        val counter = counterDao.getCounterById(counterId)

        assertNotNull(counter)
        assertEquals(TestData.validCounter.name, counter?.name)
    }

    @Test
    fun givenInvalidCounterId_getCounterById_returnsNull() = runBlocking {
        val counter = counterDao.getCounterById(1L)

        assertNull(counter)
    }

    // endregion
    // region Delete All Counters

    @Test
    fun deleteAllCounters_deletesAllCounters() = runBlocking {
        val counterId = counterDao.insertCounter(TestData.validCounter)

        counterDao.deleteAllCounters()

        assertEquals(0, counterDao.getAllCounters().size)
    }

    // endregion

// endregion
// region Test Data

// endregion
}