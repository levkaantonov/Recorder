package levkaantonov.com.study.recorder.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


private const val PREFS_NAME = "recorder_preferences"

data class RecorderPreferences(val playerTriggerAt: Long)

@Singleton
class PreferencesDataStore @Inject constructor(
    private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = PREFS_NAME)

    val recorderPreferencesFlow: Flow<RecorderPreferences> =
        context.dataStore.data
            .catch { ex ->
                if (ex is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw ex
                }
            }
            .map { prefs ->
                val triggerAt = prefs[Keys.TRIGGER_TIME] ?: 0
                RecorderPreferences(triggerAt)
            }

    suspend fun savePlayerTriggerAt(playerTriggerAt: Long) {
        context.dataStore.edit { prefs ->
            prefs[Keys.TRIGGER_TIME] = playerTriggerAt
        }
    }

    private object Keys {
        val TRIGGER_TIME = longPreferencesKey("TRIGGER_AT")
    }
}