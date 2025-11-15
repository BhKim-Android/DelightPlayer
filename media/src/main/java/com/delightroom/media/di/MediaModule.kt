package com.delightroom.media.di

import android.content.ComponentName
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
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
    fun provideExoPlayer(@ApplicationContext context: Context): Player {
        // 오디오 포커스 설정 (Service에서 하던 작업을 Module에서 처리)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        return ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true)
            .setAudioAttributes(audioAttributes, true) // 포커스 획득 요청
            .build()
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: Player
    ): MediaSession {
        return MediaSession.Builder(context, player)
            .setCallback(object : MediaSession.Callback {
                override fun onPlaybackResumption(
                    mediaSession: MediaSession,
                    controller: MediaSession.ControllerInfo
                ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
                    val currentItem = player.currentMediaItem
                    val position = player.currentPosition
                    val mediaItemsWithStart = MediaSession.MediaItemsWithStartPosition(
                        listOfNotNull(currentItem),
                        0,
                        position
                    )
                    return com.google.common.util.concurrent.Futures.immediateFuture(
                        mediaItemsWithStart
                    )
                }
            })
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
}