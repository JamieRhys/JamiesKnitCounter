package com.sycosoft.jkc.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectWithCounters(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "owning_project"
    )
    val counters: MutableList<Counter>
)