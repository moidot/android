package com.moidot.moidot.di

import com.moidot.moidot.data.repository.AuthRepositoryImpl
import com.moidot.moidot.data.repository.GroupInfoRepositoryImpl
import com.moidot.moidot.data.repository.GroupRepositoryImpl
import com.moidot.moidot.data.repository.LocationRepositoryImpl
import com.moidot.moidot.data.repository.UserRepositoryImpl
import com.moidot.moidot.repository.AuthRepository
import com.moidot.moidot.repository.GroupInfoRepository
import com.moidot.moidot.repository.GroupRepository
import com.moidot.moidot.repository.LocationRepository
import com.moidot.moidot.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGroupRepository(groupRepositoryImpl: GroupRepositoryImpl): GroupRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindGroupInfoRepository(groupInfoRepositoryImpl: GroupInfoRepositoryImpl): GroupInfoRepository
}