package com.moidot.moidot.presentation.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoidotHttpClient