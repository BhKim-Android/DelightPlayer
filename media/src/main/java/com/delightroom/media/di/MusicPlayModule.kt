package com.delightroom.media.di

import com.delightroom.domain.repository.MusicPlayRepository
import com.delightroom.media.repository.MusicPlayRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MusicPlayModule {
    @Singleton
    @Binds
    abstract fun bindMusicPlayRepository(
        musicPlayRepositoryImpl: MusicPlayRepositoryImpl
    ): MusicPlayRepository
}