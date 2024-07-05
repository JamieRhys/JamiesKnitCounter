package com.sycosoft.jkc.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_counters")
data class Counter(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "counter_name") var counterName: String,
    @ColumnInfo(name = "counter_value") var counterValue: Int = 0,
    @ColumnInfo(name = "is_linked") var isLinked: Boolean = true,
    @ColumnInfo(name = "reset_row") var resetRow: Int = 0,
    @ColumnInfo(name = "max_resets") var maxResets: Int = 0,
    @ColumnInfo(name = "show_num_resets") var showNumResets: Boolean = false,
    @ColumnInfo(name = "num_resets") var numResets: Int = 0,
    @ColumnInfo(name = "owning_project") val owningProjectId: Long,
)
