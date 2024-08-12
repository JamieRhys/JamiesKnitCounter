package com.sycosoft.jkc.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_parts")
data class Part(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("part_name") var name: String,
    @ColumnInfo("part_description") var description: String = "",
    @ColumnInfo(name = "owning_project_id") var owningProjectId: Long,
    @ColumnInfo(name = "is_current") var isCurrent: Boolean = false,
)
