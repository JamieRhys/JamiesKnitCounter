package com.sycosoft.jkc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sycosoft.jkc.database.entities.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM table_projects")
    suspend fun getAllProjects(): MutableList<Project>

    @Query("SELECT * FROM table_projects WHERE id = :id")
    suspend fun getById(id: Long): Project

    @Insert
    suspend fun addProject(project: Project): Long

    @Query("DELETE FROM table_projects WHERE id = :id")
    suspend fun removeProject(id: Long)

    @Query("DELETE FROM table_projects")
    fun deleteAllProjects()
}