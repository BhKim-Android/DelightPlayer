package com.delightroom.media.repository

import android.content.ContentUris
import android.provider.MediaStore
import android.util.Log
import androidx.concurrent.futures.await
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.delightroom.domain.model.Song
import com.delightroom.domain.repository.MusicPlayRepository
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicPlayRepositoryImpl @Inject constructor(
    private val mediaControllerFuture: ListenableFuture<MediaController>
) : MusicPlayRepository {

    private suspend fun getController(): MediaController = mediaControllerFuture.await()

    override val currentSong: Flow<Song?> = callbackFlow {
        val controller = getController()
        val listener = object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                trySend(mediaItem?.toSong())
            }
        }
        controller.addListener(listener)
        trySend(controller.currentMediaItem?.toSong())
        awaitClose { controller.removeListener(listener) }
    }

    override val progress: Flow<Long> = flow {
        val controller = getController()
        while (true) {
            emit(controller.currentPosition)
            delay(1000)
        }
    }

    override val duration: Flow<Long> = flow {
        val controller = getController()
        while (true) {
            emit(controller.duration)
            delay(1000)
        }
    }

    override suspend fun play(id: Long, songs: List<Song>) {
        val playIndex = songs.indexOfFirst { it.id == id }
        val mediaItems = songs.map {
            Log.d("toss", "setMediaId : ${it.id}\nsetUri : ${it.content}")
            Log.e("toss", "setMediaMetadata\nsetTitle: ${it.title}\nsetArtist : ${it.artist}\nsetArtworkUri : ${it.albumArt}")
            MediaItem.Builder()
                .setMediaId(it.id.toString())
                .setUri(it.content)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.title)
                        .setArtist(it.artist)
                        .setArtworkUri(it.albumArt.toUri())
                        .build()
                )
                .build()
        }

        withContext(Dispatchers.Main) {
            val controller = getController()
            controller.setMediaItems(mediaItems)
            controller.prepare()
            controller.seekTo(playIndex, 0L)
            controller.play()
        }
    }

    override suspend fun pause() = withContext(Dispatchers.Main) { getController().pause() }
    override suspend fun resume() = withContext(Dispatchers.Main) { getController().play() }
    override suspend fun stop() = withContext(Dispatchers.Main) { getController().stop() }
    override suspend fun seekTo(positionMs: Long) =
        withContext(Dispatchers.Main) { getController().seekTo(positionMs) }

    override suspend fun next() =
        withContext(Dispatchers.Main) { getController().seekToNextMediaItem() }

    override suspend fun previous() =
        withContext(Dispatchers.Main) { getController().seekToPreviousMediaItem() }

    private fun MediaItem.toSong(): Song? {
        return Song(
            id = mediaId.toLong(),
            title = mediaMetadata.title.toString(),
            artist = mediaMetadata.artist.toString(),
            albumArt = mediaMetadata.artworkUri.toString(),
            content = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                mediaId.toLong()
            ).toString()
        )
    }
}