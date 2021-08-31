package levkaantonov.com.study.recorder.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import levkaantonov.com.study.recorder.R
import levkaantonov.com.study.recorder.db.Record
import levkaantonov.com.study.recorder.db.RecordDao
import levkaantonov.com.study.recorder.db.RecordsDb
import levkaantonov.com.study.recorder.ui.activities.MainActivity
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat

class RecordService : Service() {
    private val CHANNEL_ID = getString(R.string.recordService)

    private var _fileName: String? = null
    private var _path: String? = null
    private var _countOfRecords: Int? = null
    private var _recorder: MediaRecorder? = null
    private var _startTimeMillis: Long = 0

    private var _endTimeMillis: Long = 0

    private var _recordDao: RecordDao? = null
    private val job = Job()
    private val _uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        _recordDao = RecordsDb.getInstance(application).recordDao
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        _countOfRecords = intent?.extras?.get("COUNT") as Int?
        startRecording()
        return START_STICKY
    }

    override fun onDestroy() {
        if (_recorder != null)
            stopRecording()
        super.onDestroy()
    }

    private fun startRecording() {
        setFileNameAndPath()

        _recorder = MediaRecorder()
        _recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(_path)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioChannels(1)
            setAudioEncodingBitRate(192000)
        }

        try {
            _recorder?.apply {
                prepare()
                start()
                _startTimeMillis = System.currentTimeMillis()
                startForeground(1, createNotification())
            }
        } catch (e: IOException) {
            Log.e("TAG", "prepare failed ", e)
        }
    }

    private fun stopRecording() {


        _recorder?.apply {
            stop()
            release()
        }

        Toast.makeText(
            this,
            getString(R.string.recording_finished),
            Toast.LENGTH_SHORT
        ).show()

        val record = Record(
            name = _fileName.toString(),
            path = _path.toString(),
            length = System.currentTimeMillis() - _startTimeMillis,
            timeOfCreation = System.currentTimeMillis()
        )

        _recorder = null


        try {
            _uiScope.launch {
                withContext(Dispatchers.IO) {
                    _recordDao?.create(record)
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "exception: ", e)
        }
    }

    private fun createNotification(): Notification? {
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mic)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.recording))
            .setOngoing(true)

        builder.setContentIntent(
            PendingIntent.getActivities(
                applicationContext, 0, arrayOf(
                    Intent(
                        applicationContext,
                        MainActivity::class.java
                    )
                ), 0
            )
        )

        return builder.build()
    }

    private fun setFileNameAndPath() {
        var count = 0
        var file: File
        val dateTime = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
            .format(System.currentTimeMillis())

        do {
            _fileName = getString(R.string.default_file_name) +
                    "_" + dateTime + count + getString(R.string.mp4)
            _path = applicationContext.getExternalFilesDir(null)?.absolutePath
            _path += "/$_path"

            count++

            file = File(_path)
        } while (file.exists() && !file.isDirectory)
    }
}