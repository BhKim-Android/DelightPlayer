package com.delightroom.media.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.delightroom.domain.repository.MusicPlayRepository
import com.delightroom.media.repository.MusicPlayRepositoryImpl
import com.delightroom.media.service.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .build()
    }

    @Provides
    @Singleton
    fun provideSessionToken(
        @ApplicationContext context: Context
    ): SessionToken {
        return SessionToken(context, ComponentName(context, PlaybackService::class.java))
    }

    @Provides
    @Singleton
    fun provideMediaControllerFuture(
        @ApplicationContext context: Context,
        sessionToken: SessionToken
    ): ListenableFuture<MediaController> {
        return MediaController.Builder(context, sessionToken).buildAsync()
    }

    @Singleton
    @Provides
    fun provideMusicPlayRepository(
        mediaControllerFuture: ListenableFuture<MediaController>
    ): MusicPlayRepository {
        return MusicPlayRepositoryImpl(mediaControllerFuture)
    }
}