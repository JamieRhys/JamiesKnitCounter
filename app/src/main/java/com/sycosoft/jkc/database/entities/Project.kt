package com.sycosoft.jkc.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sycosoft.jkc.util.ProjectType
import java.time.LocalDateTime

/** This class represents a project within the application.
 *
 * @property id The unique identifier for the project. This is provided by the database.
 * @property name The name of the project.
 * @property description A description of what the project is.
 *
 * @author Jamie-Rhys Edwards
 * @since v0.0.1
 */
@Entity(tableName = "table_projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "project_name") var name: String,
    @ColumnInfo(name = "project_description") var description: String = "",
    @ColumnInfo(name = "project_completed") var completed: Boolean = false,
    //@ColumnInfo(name = "project_date_started") val dateStarted: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "project_type") var type: ProjectType = ProjectType.Knitting,
)