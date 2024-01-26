package com.example.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.common.data.db.dao.PlaylistDao
import com.example.playlistmaker.common.data.db.dao.TrackDao
import com.example.playlistmaker.common.data.db.dao.TracksPlaylistDao
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.common.data.db.entity.TrackEntity
import com.example.playlistmaker.common.data.db.entity.TracksPlaylistEntity
import com.example.playlistmaker.util.PlaylistTracksConverter
import com.example.playlistmaker.util.TracksPlaylistConverter

@Database(
    version = 3,
    entities = [TrackEntity::class, PlaylistEntity::class, TracksPlaylistEntity::class]
)
@TypeConverters(PlaylistTracksConverter::class, TracksPlaylistConverter::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun tracksPlaylistDao(): TracksPlaylistDao


    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS playlists (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "name TEXT NOT NULL, " +
                            "description TEXT NOT NULL, " +
                            "imageName TEXT, " +
                            "tracks TEXT, " +
                            "tracksNumber INTEGER NOT NULL DEFAULT 0)"
                )
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS tracks_playlist (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "playlistId INTEGER NOT NULL, " +
                            "track TEXT NOT NULL)"
                )
            }
        }
    }
}

