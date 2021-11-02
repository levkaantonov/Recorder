package levkaantonov.com.study.recorder.ui.fragments.remove_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import levkaantonov.com.study.recorder.data.db.RecordsRepository
import java.io.File
import javax.inject.Inject

class RemoveRecordViewModel @Inject constructor(
    private val recordsRepository: RecordsRepository
) : ViewModel() {
    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun removeItem(itemId: Int) {
        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    recordsRepository.deleteById(itemId)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeFile(path: String) {
        val file = File(path)
        if (!file.exists()) {
            return
        }

        file.delete()
    }


    class RemoveRecordViewModelFactory @Inject constructor(
        private val recordsRepository: RecordsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == RemoveRecordViewModel::class.java)
            return RemoveRecordViewModel(recordsRepository) as T
        }
    }
}