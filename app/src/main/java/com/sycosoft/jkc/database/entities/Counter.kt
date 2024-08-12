package com.sycosoft.jkc.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sycosoft.jkc.util.CounterType

/** The clas that represents a counter within the application.
 *
 * @property id The unique identifier for the counter. This is provided by the database.
 * @property name The name of the counter.
 * @property value The current value of the counter.
 * @property counterType The type of counter this is representing. see [CounterType] for available types.
 * @property isGloballyLinked Whether or not the counter is linked to the Global Counter. (If the counter is a [CounterType.Stitch] then this will be false.)
 * @property resetRow The row number at which the counter should be reset back to a given number. If set to 0, the counter will not be reset.
 * @property maxResets The maximum number of times the counter will reset. If set to 0, this is ignored.
 * @property owningPartId The unique identifier for the [Part] that owns this counter.
 *
 * @author Jamie-Rhys Edwards
 * @since v0.0.1
 */
@Entity(tableName = "table_counters")
data class Counter(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "counter_name") var name: String,
    @ColumnInfo(name = "counter_value") var value: Long = 0,
    @ColumnInfo(name = "increment_by") var incrementBy: Int = 1,
    @ColumnInfo(name = "counter_type") val counterType: CounterType = CounterType.Normal,
    @ColumnInfo(name = "is_globally_linked") var isGloballyLinked: Boolean = true,
    @ColumnInfo(name = "reset_row") var resetRow: Long = 0,
    @ColumnInfo(name = "max_resets") var maxResets: Long = 0,
    @ColumnInfo(name = "owning_part_id") val owningPartId: Long,
) {
    fun increment() {
        value += incrementBy
    }

    fun decrement() {
        value -= incrementBy
    }

    fun globallyLinked() {
        isGloballyLinked = !isGloballyLinked
    }
}
