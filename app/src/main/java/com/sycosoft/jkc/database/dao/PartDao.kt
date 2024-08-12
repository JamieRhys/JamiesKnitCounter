package com.sycosoft.jkc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sycosoft.jkc.database.entities.Part

@Dao
interface PartDao {
    @Query("SELECT * FROM table_parts")
    fun getAllParts(): List<Part>

    @Query("SELECT * FROM table_parts WHERE owning_project_id = :projectId")
    suspend fun getProjectParts(projectId: Long): List<Part>

    @Query("SELECT EXISTS(SELECT 1 FROM table_parts WHERE id = :partId)")
    fun partExists(partId: Long): Boolean

    @Query("SELECT * FROM table_parts WHERE id = :id")
    fun getPartById(id: Long): Part?

    @Insert
    fun insertPart(part: Part): Long

    @Update
    fun updatePart(part: Part): Int

    @Query("DELETE FROM table_parts WHERE id = :id")
    fun deletePartById(id: Long): Int

    @Query("DELETE FROM table_parts")
    fun deleteAllParts()
}