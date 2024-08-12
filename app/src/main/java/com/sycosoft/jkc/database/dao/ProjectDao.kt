package com.sycosoft.jkc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sycosoft.jkc.database.entities.Project

/** This class is responsible for interfacing with the database to retrieve and store project data.
 *
 * @author Jamie-Rhys Edwards
 * @since v0.0.1
 */
@Dao
interface ProjectDao {
    @Query("SELECT * FROM table_projects")
    suspend fun getAllProjects(): List<Project>

    @Query("SELECT * FROM table_projects WHERE id = :id")
    suspend fun getProjectById(id: Long): Project?

    @Insert
    suspend fun insertProject(project: Project): Long

    @Query("SELECT EXISTS (SELECT 1 FROM table_projects WHERE id = :id)")
    suspend fun projectExists(id: Long): Boolean

    @Query("DELETE FROM table_projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long): Int

    @Query("DELETE FROM table_projects")
    fun deleteAllProjects()
}