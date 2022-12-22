package com.damonv.trialvideoapplication.data.db

import androidx.room.*

@Database(entities = [VideoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao

    @Dao
    interface VideoDao {
        @Transaction
        @Query("SELECT * FROM video")
        suspend fun getList(): List<VideoEntity>

        @Transaction
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertList(list: List<VideoEntity>): List<Long>
    }
}