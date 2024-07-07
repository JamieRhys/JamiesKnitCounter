package com.sycosoft.jkc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sycosoft.jkc.database.dao.CounterDao
import com.sycosoft.jkc.database.dao.ProjectDao
import com.sycosoft.jkc.database.entities.Counter
import com.sycosoft.jkc.database.entities.Project
import com.sycosoft.jkc.database.entities.ProjectWithCounters

/**
 * This manages our applications database connection.
 *
 * @author Jamie-Rhys Edwards
 * @since v0.0.1
 *
 */
// Let's define the databases configuration
@Database(
    entities = [Project::class, Counter::class], // List of entity classes that will be a part of the database.
    version = 4 // The version of the database.
)
abstract class AppDatabase: RoomDatabase() {
    abstract val projectDao: ProjectDao
    abstract val counterDao: CounterDao

    companion object {
        // This volatile variable ensures that the value is read from and written to main memory directly,
        // avoiding the use of a CPU cache, making it safe for multithreaded access.
        @Volatile var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If the instance is null, the database has not yet been initialised.
            if(INSTANCE == null) {
                // Synchronised block ensures that only one thread can execute the code within it
                // at any one time, preventing multiple threads from creating duplicate instances
                // of the database.
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "jkc.db",
                    )
                        .fallbackToDestructiveMigration() // Set to allow destructive migration,
                        // meaning if the database schema changes, it will drop the existing
                        // database and recreate it.
                        // TODO: Remove fallbackToDestructiveMigration for production release as we do not want this!
                        .build()
                }
            }

            // If the instance is not null, we've previously initialised the database (creating a
            // singleton class), so return the instance.
            return INSTANCE!!
        }
    }
}