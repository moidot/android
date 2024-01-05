package com.moidot.moidot.presentation.di

import com.moidot.moidot.data.repository.AuthRepositoryImpl
import com.moidot.moidot.data.repository.UserRepositoryImpl
import com.moidot.moidot.domain.repository.AuthRepository
import com.moidot.moidot.domain.repository.UserRepository
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
}