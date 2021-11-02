package levkaantonov.com.study.recorder.data.preferences

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: PreferencesDataStore
) : PreferencesRepository {

    override val recorderFlow: Flow<RecorderPreferences> = dataStore.recorderPreferencesFlow

    override suspend fun saveTriggerAt(triggerAt: Long) {
        dataStore.savePlayerTriggerAt(triggerAt)
    }
}