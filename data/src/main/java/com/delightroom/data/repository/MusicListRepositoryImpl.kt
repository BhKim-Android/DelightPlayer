package com.delightroom.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.net.toUri
import com.delightroom.domain.model.Song
import com.delightroom.domain.repository.MusicListRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class MusicListRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : MusicListRepository {
//    override fun getSongList(): Flow<PagingData<Song>> =
//        Pager(
//            config = PagingConfig(pageSize = 10, enablePlaceholders = false, initialLoadSize = 10),
//            pagingSourceFactory = { musicPagingSourceProvider.get() }
//        ).flow

    override suspend fun getSongList(): Result<List<Song>> = runCatching {
        suspendCancellableCoroutine { continuation ->
            try {
                val songList = mutableListOf<Song>()

                val projection = arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.ARTIST
                )

                val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val queryArgs = Bundle().apply {
                        putString(
                            ContentResolver.QUERY_ARG_SQL_SELECTION,
                            "${MediaStore.Audio.Media.IS_MUSIC} != 0"
                        )
                        putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, arrayOf())
                        putString(
                            ContentResolver.QUERY_ARG_SORT_COLUMNS,
                            MediaStore.Audio.Media.DATE_ADDED
                        )
                        putInt(
                            ContentResolver.QUERY_ARG_SORT_DIRECTION,
                            ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                        )
                    }
                    context.contentResolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        queryArgs,
                        null
                    )
                } else {
                    context.contentResolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        "${MediaStore.Audio.Media.IS_MUSIC} != 0",
                        null,
                        "${MediaStore.Audio.Media.DATE_ADDED} DESC"
                    )
                }

                cursor?.use { c ->
                    val idColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val titleColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                    val albumIdColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val artistColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

                    while (c.moveToNext()) {
                        val id = c.getLong(idColumn)
                        val title = c.getString(titleColumn) ?: "Unknown Title"
                        val albumId = c.getLong(albumIdColumn)
                        val artist = c.getString(artistColumn) ?: "Unknown Artist"

                        val albumArt = ContentUris.withAppendedId(
                            "content://media/external/audio/albumart".toUri(),
                            albumId
                        ).toString()

                        val content = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                        ).toString()

                        songList.add(
                            Song(
                                id = id,
                                title = title,
                                artist = artist,
                                albumArt = albumArt,
                                content = content
                            )
                        )
                    }
                }
                continuation.resumeWith(Result.success(songList))
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }
    }
}