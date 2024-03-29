package com.moidot.moidot.di

import com.moidot.moidot.data.local.datasource.location.LocationLocalDataSource
import com.moidot.moidot.data.local.datasource.location.LocationLocalDataSourceImpl
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSource
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSourceImpl
import com.moidot.moidot.data.remote.datasource.auth.AuthRemoteDataSource
import com.moidot.moidot.data.remote.datasource.auth.AuthRemoteDataSourceImpl
import com.moidot.moidot.data.remote.datasource.group.GroupInfoRemoteDataSource
import com.moidot.moidot.data.remote.datasource.group.GroupInfoRemoteDataSourceImpl
import com.moidot.moidot.data.remote.datasource.group.GroupPlaceRemoteDataSource
import com.moidot.moidot.data.remote.datasource.group.GroupPlaceRemoteDataSourceImpl
import com.moidot.moidot.data.remote.datasource.group.GroupRemoteDataSource
import com.moidot.moidot.data.remote.datasource.group.GroupRemoteDataSourceImpl
import com.moidot.moidot.data.remote.datasource.group.GroupVoteRemoteDataSource
import com.moidot.moidot.data.remote.datasource.group.GroupVoteRemoteDataSourceImpl
import com.moidot.moidot.data.remote.datasource.location.LocationRemoteDataSource
import com.moidot.moidot.data.remote.datasource.location.LocationRemoteDataSourceImpl
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

    @Binds
    abstract fun bindLocationLocalSource(locationLocalDataSourceImpl: LocationLocalDataSourceImpl): LocationLocalDataSource

    @Binds
    abstract fun bindLocationRemoteSource(locationRemoteDataSourceImpl: LocationRemoteDataSourceImpl): LocationRemoteDataSource

    @Binds
    abstract fun bindGroupInfoRemoteSource(groupInfoRemoteDataSourceImpl: GroupInfoRemoteDataSourceImpl): GroupInfoRemoteDataSource

    @Binds
    abstract fun bindGroupPlaceRemoteSource(groupPlaceRemoteDataSourceImpl: GroupPlaceRemoteDataSourceImpl): GroupPlaceRemoteDataSource

    @Binds
    abstract fun bingGroupVoteRemoteSource(groupVoteRemoteDataSourceImpl: GroupVoteRemoteDataSourceImpl): GroupVoteRemoteDataSource
}