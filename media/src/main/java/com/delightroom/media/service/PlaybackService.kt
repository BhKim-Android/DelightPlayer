package com.delightroom.media.service

import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {

    @Inject
    lateinit var player: Player

    @Inject
    lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()
//        val player = ExoPlayer.Builder(this)
//            .setHandleAudioBecomingNoisy(true)  // 이어폰을 뽑으면 자동 pause
//            .build()
//        mediaSession = MediaSession.Builder(this, player)
//            .setCallback(object : MediaSession.Callback {
//                override fun onPlaybackResumption(
//                    mediaSession: MediaSession,
//                    controller: MediaSession.ControllerInfo
//                ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
//                    // 여기에 재생할 MediaItem을 반환
//                    // 예시: 현재 플레이어의 MediaItem과 위치를 반환
//                    val currentItem = player.currentMediaItem
//                    val position = player.currentPosition
//                    val mediaItemsWithStart = MediaSession.MediaItemsWithStartPosition(
//                        listOfNotNull(currentItem),
//                        0, // currentItem이 첫 번째라면 index = 0
//                        position
//                    )
//                    return com.google.common.util.concurrent.Futures.immediateFuture(
//                        mediaItemsWithStart
//                    )
//                }
//            })
//            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession.player.release()
        mediaSession.release()
        super.onDestroy()
//        mediaSession?.run {
//            player.release()
//            release()
//            mediaSession = null
//        }
//        super.onDestroy()
    }
}