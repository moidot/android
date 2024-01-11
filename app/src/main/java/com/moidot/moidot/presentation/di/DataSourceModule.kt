package com.moidot.moidot.presentation.di

import com.moidot.moidot.data.local.datasource.user.UserLocalDataSource
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSourceImpl
import com.moidot.moidot.data.remote.datasource.auth.AuthRemoteDataSource
import com.moidot.moidot.data.remote.datasource.auth.AuthRemoteDataSourceImpl
import com.moidot.moidot.data.remote.datasource.group.GroupRemoteDataSource
import com.moidot.moidot.data.remote.datasource.group.GroupRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindUserLocalSource(userLocalDataSourceImpl: UserLocalDataSourceImpl): UserLocalDataSource

    @Binds
    abstract fun bindAuthRemoteSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    abstract fun bindGroupRemoteSource(groupRemoteDataSourceImpl: GroupRemoteDataSourceImpl): GroupRemoteDataSource
}