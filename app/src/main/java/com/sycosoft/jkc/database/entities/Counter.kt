package com.sycosoft.jkc.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_counters")
data class Counter(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "is_global") val isGlobal: Boolean = false,
    @ColumnInfo(name = "is_stitch") val isStitch: Boolean = false,
    @ColumnInfo(name = "counter_name") var counterName: String,
    @ColumnInfo(name = "counter_value") var counterValue: Int = 0,
    @ColumnInfo(name = "is_linked") var isLinked: Boolean = true,
    @ColumnInfo(name = "show_link") val showLink: Boolean = true,
    @ColumnInfo(name = "reset_row") var resetRow: Int = 0,
    @ColumnInfo(name = "can_reset") var canReset: Boolean = true,
    @ColumnInfo(name = "max_resets") var maxResets: Int = 0,
    @ColumnInfo(name = "show_num_resets") var showNumResets: Boolean = false,
    @ColumnInfo(name = "num_resets") var numResets: Int = 0,
    @ColumnInfo(name = "can_be_user_deleted") val canBeUserDeleted: Boolean = true,
    @ColumnInfo(name = "owning_project") val owningProjectId: Long,
)
