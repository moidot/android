package com.moidot.moidot.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moidot.moidot.data.local.dao.PlaceDao
import com.moidot.moidot.data.local.entity.PlaceEntity

@Database(entities = [PlaceEntity::class], version = 1)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}