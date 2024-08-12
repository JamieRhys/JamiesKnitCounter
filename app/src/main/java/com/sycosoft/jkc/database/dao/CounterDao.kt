package com.sycosoft.jkc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sycosoft.jkc.database.entities.Counter

/** This class is responsible for interfacing with the database to store and retrieve counter data.
 *
 * @author Jamie-Rhys Edwards
 * @since v0.0.1
 */
@Dao
interface CounterDao {
    @Query("SELECT * FROM table_counters")
    fun getAllCounters(): List<Counter>

    @Query("SELECT * FROM table_counters WHERE owning_part_id = :projectId")
    suspend fun getPartCounters(projectId: Long): List<Counter>

    @Query("SELECT EXISTS(SELECT 1 FROM table_counters WHERE id = :counterId)")
    fun counterExists(counterId: Long): Boolean

    @Insert
    fun insertCounter(counter: Counter): Long

    @Update
    fun updateCounter(counter: Counter): Int

    @Query("DELETE FROM table_counters WHERE id = :id")
    fun deleteCounterById(id: Long): Int

    @Query("SELECT * FROM table_counters WHERE id = :id")
    fun getCounterById(id: Long): Counter?

    @Query("DELETE FROM table_counters")
    fun deleteAllCounters()

}