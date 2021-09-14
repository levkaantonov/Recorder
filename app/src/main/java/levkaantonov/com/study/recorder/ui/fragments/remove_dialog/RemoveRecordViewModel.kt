package levkaantonov.com.study.recorder.ui.fragments.remove_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import levkaantonov.com.study.recorder.db.RecordDao
import java.io.File

class RemoveRecordViewModel(
    private var recordDao: RecordDao
) : ViewModel() {
    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun removeItem(itemId: Int) {
        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    recordDao.deleteById(itemId)
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
}

class RemoveRecordViewModelFactory(
    private val recordDao: RecordDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoveRecordViewModel::class.java)) {
            return RemoveRecordViewModel(
                recordDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}