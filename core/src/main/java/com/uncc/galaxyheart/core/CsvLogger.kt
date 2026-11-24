package com.uncc.galaxyheart.core

import android.content.Context
import java.io.BufferedWriter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Generic per-session CSV writer shared by every sensor app.
 *
 * Files land in the app's own external files dir (no runtime permission needed):
 *   Android/data/<applicationId>/files/sessions/<prefix>_<timestamp>.csv
 *
 * Pull them off the watch with:
 *   adb pull /sdcard/Android/data/<applicationId>/files/sessions ./
 *
 * @param filePrefix e.g. "hr", "ecg", "bia"
 * @param header CSV header line (no trailing newline), e.g. "timestamp_ms,bpm"
 */
class CsvLogger(
    private val context: Context,
    private val filePrefix: String,
    private val header: String,
) {
    private var writer: BufferedWriter? = null

    var currentFile: File? = null
        private set

    /** Opens a new session file with the header and returns it. */
    fun start(): File {
        val dir = File(context.getExternalFilesDir(null), "sessions").apply { mkdirs() }
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = File(dir, "${filePrefix}_session_$stamp.csv")
        writer = file.bufferedWriter().apply { appendLine(header) }
        currentFile = file
        return file
    }

    /** Appends one already-formatted CSV row (no trailing newline needed). */
    fun write(row: String) {
        val w = writer ?: return
        w.appendLine(row)
        w.flush() // small data rate; flush so nothing is lost if the app is killed
    }

    fun stop() {
        writer?.flush()
        writer?.close()
        writer = null
        currentFile = null
    }
}
