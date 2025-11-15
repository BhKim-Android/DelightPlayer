package com.delightroom.media.repository

import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicPlayRepositoryImpl @Inject constructor(
    private val mediaControllerFuture: ListenableFuture<MediaController>
) : MusicPlayRepository {

    // 비동기 MediaController를 안전하게 가져오기
    private suspend fun getController(): MediaController = mediaControllerFuture.await()

    override val progress: Flow<Long> = callbackFlow {
        val controller = getController()
        while (true) {
            trySend(controller.currentPosition)
            delay(1000)
        }
        awaitClose { }
    }

    override suspend fun play(
        songs: List<Song>,
        index: Int
    ) {
        val controller = getController()

        val mediaItems = songs.map { song ->
            MediaItem.Builder()
                .setMediaId(song.id.toString())
                .setUri(song.content)
                .build()
        }
        controller.setMediaItems(mediaItems)
        controller.seekTo(index, 0L)
        controller.prepare()
        controller.play()
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
}