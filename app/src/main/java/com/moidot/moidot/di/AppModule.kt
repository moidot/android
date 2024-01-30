package com.moidot.moidot.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.moidot.moidot.data.local.dao.PlaceDao
import com.moidot.moidot.data.local.db.PlaceDatabase
import com.moidot.moidot.MoidotApplication.Companion.MOIDOT_APP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(MOIDOT_APP, MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providePlaceDatabase(@ApplicationContext context: Context): PlaceDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            PlaceDatabase::class.java,
            "favorite-places"
        ).build()

    @Provides
    @Singleton
    fun providesPlaceDao(database: PlaceDatabase): PlaceDao {
        return database.placeDao()
    }
}