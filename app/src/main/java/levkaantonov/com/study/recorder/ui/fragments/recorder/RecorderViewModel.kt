package levkaantonov.com.study.recorder.ui.fragments.recorder

import android.os.CountDownTimer
import android.os.SystemClock
import androidx.lifecycle.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import levkaantonov.com.study.recorder.data.db.RecordsRepository
import levkaantonov.com.study.recorder.data.preferences.PreferencesRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RecorderViewModel @Inject constructor(
    private val prefs: PreferencesRepository,
    recordsRepository: RecordsRepository
) : ViewModel() {

    private val _elapsedTime = MutableLiveData<String>()
    val elapsedTime: LiveData<String> = _elapsedTime
    val countOfRecords: LiveData<Int> = recordsRepository.getCount()
    private val second: Long = 1000L
    private var timer: CountDownTimer? = null

    init {
        createTimer()
    }

    fun timeFormatter(time: Long): String {
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(time) % 60,
            TimeUnit.MILLISECONDS.toMinutes(time) % 60,
            TimeUnit.MILLISECONDS.toSeconds(time) % 60
        )
    }

    fun stopTimer() {
        timer?.cancel()
        resetTimer()
    }

    fun resetTimer() {
        _elapsedTime.value = timeFormatter(0)
        viewModelScope.launch {
            saveTime(0)
        }
    }

    fun startTimer() {
        val triggerTime = SystemClock.elapsedRealtime()
        viewModelScope.launch {
            saveTime(triggerTime)
            createTimer()
        }
    }

    private fun createTimer() {
        viewModelScope.launch {
            val triggerTime = prefs.recorderFlow.first()
            timer = object : CountDownTimer(triggerTime.playerTriggerAt, second) {
                override fun onTick(millisUntilFinished: Long) {
                    _elapsedTime.value =
                        timeFormatter(SystemClock.elapsedRealtime() - triggerTime.playerTriggerAt)
                }

                override fun onFinish() {
                    resetTimer()
                }
            }.apply { start() }
        }
    }

    private suspend fun saveTime(triggerTime: Long) = prefs.saveTriggerAt(triggerTime)

    class RecorderViewModelFactory @Inject constructor(
        private val prefs: PreferencesRepository,
        private val recordsRepository: RecordsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == RecorderViewModel::class.java)
            return RecorderViewModel(prefs, recordsRepository) as T
        }
    }
}