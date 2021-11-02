package levkaantonov.com.study.recorder.data.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val recorderFlow: Flow<RecorderPreferences>
    suspend fun saveTriggerAt(triggerAt: Long)
}