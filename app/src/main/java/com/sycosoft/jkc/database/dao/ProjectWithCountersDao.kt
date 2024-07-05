package com.sycosoft.jkc.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.sycosoft.jkc.database.entities.ProjectWithCounters

@Dao
interface ProjectWithCountersDao {
    @Transaction
    @Query("SELECT * FROM table_projects WHERE id = :projectId")
    fun getProjectWithCounters(projectId: Long): ProjectWithCounters
}