package com.moidot.moidot.presentation.di

import com.moidot.moidot.data.api.AuthService
import com.moidot.moidot.data.api.GroupInfoService
import com.moidot.moidot.data.api.GroupService
import com.moidot.moidot.data.api.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideGroupService(retrofit: Retrofit): GroupService {
        return retrofit.create(GroupService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationService(@KakaoHttpClient retrofit: Retrofit): LocationService {
        return retrofit.create(LocationService::class.java)
    }

    @Provides
    @Singleton
    fun provideGroupInfoService(retrofit: Retrofit): GroupInfoService {
        return retrofit.create(GroupInfoService::class.java)
    }
}