package levkaantonov.com.study.recorder.services

import android.annotation.SuppressLint
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
import levkaantonov.com.study.recorder.appComponent
import levkaantonov.com.study.recorder.data.RecordsRepository
import levkaantonov.com.study.recorder.data.db.Record
import levkaantonov.com.study.recorder.ui.activities.MainActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import javax.inject.Inject

class RecordService : Service() {

    @Inject
    lateinit var recordsRepository: RecordsRepository

    private var _fileName: String? = null
    private var _path: String? = null
    private var _countOfRecords: Int? = null
    private var _recorder: MediaRecorder? = null
    private var _startTimeMillis: Long = 0

    private var _endTimeMillis: Long = 0

    private val job = Job()
    private val _uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()
        application.appComponent.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

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
        Log.d("TAG", "startRecording: $_path")
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
                    recordsRepository.create(record)
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "exception: ", e)
        }
    }

    private fun createNotification(): Notification? {
        val builder = NotificationCompat.Builder(
            applicationContext,
            getString(R.string.notification_channel_id)
        )
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

    @SuppressLint("SimpleDateFormat")
    private fun setFileNameAndPath() {
        var count = 0
        var file: File
        val dateTime = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
            .format(System.currentTimeMillis())

        do {
            _fileName = String.format(
                "%s_%s_%d%s",
                getString(R.string.default_file_name),
                dateTime,
                count,
                getString(R.string.mp4)
            )
            _path = String.format(
                "%s/%s",
                applicationContext.getExternalFilesDir(null)?.absolutePath,
                _fileName
            )
            count++
            file = File(_path)
        } while (file.exists() && !file.isDirectory)
    }
}