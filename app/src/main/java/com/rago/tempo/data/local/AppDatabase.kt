package com.rago.tempo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rago.tempo.data.local.dao.UserDao
import com.rago.tempo.data.local.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
