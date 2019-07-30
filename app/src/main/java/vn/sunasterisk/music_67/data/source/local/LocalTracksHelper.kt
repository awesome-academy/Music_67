package vn.sunasterisk.music_67.data.source.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import vn.sunasterisk.music_67.data.model.Track

private const val SQL_CREATE_ENTRIES =
		"CREATE TABLE ${LocalTracksContract.TABLE_NAME} (" +
				"${LocalTracksContract.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
				"${LocalTracksContract.COLUMN_NAME_TITLE} TEXT," +
				"${LocalTracksContract.COLUMN_NAME_ARTIST} TEXT," +
				"${LocalTracksContract.COLUMN_NAME_ARTWORK_URL} TEXT," +
				"${LocalTracksContract.COLUMN_NAME_DURATION} BIGINT," +
				"${LocalTracksContract.COLUMN_NAME_STREAM_URL} TEXT)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${LocalTracksContract.TABLE_NAME}"

class LocalTracksHelper(context: Context)
	: SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
	val dbHelper = LocalTracksHelper(context)
	override fun onCreate(db: SQLiteDatabase) {
		db.execSQL(SQL_CREATE_ENTRIES)
	}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
		db.execSQL(SQL_DELETE_ENTRIES)
		onCreate(db)
	}

	override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
		onUpgrade(db, oldVersion, newVersion)
	}

	fun insertDownloaded(track: Track): Long {
		val db = dbHelper.writableDatabase
		val values = ContentValues().apply {
			put(LocalTracksContract.COLUMN_NAME_ID, track.id)
			put(LocalTracksContract.COLUMN_NAME_TITLE, track.title)
			put(LocalTracksContract.COLUMN_NAME_ARTIST, track.artist)
			put(LocalTracksContract.COLUMN_NAME_ARTWORK_URL, track.artworkUrl)
			put(LocalTracksContract.COLUMN_NAME_DURATION, track.duration)
			put(LocalTracksContract.COLUMN_NAME_STREAM_URL, track.streamUrl)
		}
		val newRowId = db.insert(LocalTracksContract.TABLE_NAME, null, values)
		return newRowId
	}

	fun getAllDownloadedTracks(): Cursor {
		val db = dbHelper.readableDatabase
		val projection = arrayOf(LocalTracksContract.COLUMN_NAME_ID,
				LocalTracksContract.COLUMN_NAME_TITLE,
				LocalTracksContract.COLUMN_NAME_ARTIST,
				LocalTracksContract.COLUMN_NAME_ARTWORK_URL,
				LocalTracksContract.COLUMN_NAME_DURATION)
		val cursor = db.query(
				LocalTracksContract.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				null
		)
		return cursor
	}

	fun searchDownloadedTracks(searchString: String): Cursor {
		val db = dbHelper.readableDatabase
		val projection = arrayOf(LocalTracksContract.COLUMN_NAME_ID,
				LocalTracksContract.COLUMN_NAME_TITLE,
				LocalTracksContract.COLUMN_NAME_ARTIST,
				LocalTracksContract.COLUMN_NAME_ARTWORK_URL,
				LocalTracksContract.COLUMN_NAME_DURATION)
		val selection = "${LocalTracksContract.COLUMN_NAME_TITLE} = ?"
		val selectionArgs = arrayOf(searchString)
		val cursor = db.query(
				LocalTracksContract.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		)
		return cursor
	}

	fun removeOneDownloadedTrack(track: Track): Int {
		val db = dbHelper.writableDatabase
		val selection = "${LocalTracksContract.COLUMN_NAME_TITLE} LIKE ?"
		val selectionArgs = arrayOf(track.title)
		val deletedRows = db.delete(LocalTracksContract.TABLE_NAME, selection, selectionArgs)
		return deletedRows
	}

	companion object {
		const val DATABASE_VERSION = 1
		const val DATABASE_NAME = "Musicque.db"
	}
}
