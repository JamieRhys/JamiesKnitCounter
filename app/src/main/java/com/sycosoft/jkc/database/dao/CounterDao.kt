package com.sycosoft.jkc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sycosoft.jkc.database.entities.Counter


@Dao
interface CounterDao {
    @Query("SELECT * FROM table_counters")
    fun getAll(): MutableList<Counter>

    @Query("SELECT * FROM table_counters WHERE id = :id")
    fun getProjectCounters(id: Long): MutableList<Counter>

    @Insert
    fun addCounter(counter: Counter)

    @Query("DELETE FROM table_counters")
    fun deleteAllCounters()
}