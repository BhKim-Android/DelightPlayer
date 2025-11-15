package com.delightroom.data.paging

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.delightroom.domain.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MusicPagingSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PagingSource<Int, Song>() {
    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        val offset = params.key ?: 0
        val pageSize = params.loadSize

        val songList = mutableListOf<Song>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST
        )

        try {
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
                    putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
                    putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                }
                context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    queryArgs,
                    null
                )
            } else {
                context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon()
                        .appendQueryParameter("limit", pageSize.toString())
                        .appendQueryParameter("offset", offset.toString())
                        .build(),
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

                    val song = Song(id, content, albumArt, title, artist)

                    songList.add(song)
                }
            }

            return LoadResult.Page(
                data = songList,
                prevKey = if (offset == 0) null else offset,
                nextKey = if (songList.isEmpty()) null else offset + songList.size
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}