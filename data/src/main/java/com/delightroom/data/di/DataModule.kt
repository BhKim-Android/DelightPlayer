package com.delightroom.data.di

import com.delightroom.data.repository.MusicListRepositoryImpl
import com.delightroom.domain.repository.MusicListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        musicListRepositoryImpl: MusicListRepositoryImpl
    ): MusicListRepository
}