package com.sycosoft.jkc.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "project_name") var projectName: String,
    @ColumnInfo(name = "project_description") var projectDescription: String = "",
    @ColumnInfo(name = "rows_completed") var rowsCompleted: Int = 0,
)

