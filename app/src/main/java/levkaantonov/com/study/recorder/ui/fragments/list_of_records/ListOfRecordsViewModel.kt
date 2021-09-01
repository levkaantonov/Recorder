package levkaantonov.com.study.recorder.ui.fragments.list_of_records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import levkaantonov.com.study.recorder.db.RecordDao
import java.lang.IllegalArgumentException

class ListOfRecordsViewModel(
    private val recordDao: RecordDao
) : ViewModel() {
    val records = recordDao?.getALl()
}

class ListOfRecordsViewModelFactory(
    private val recordDao: RecordDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListOfRecordsViewModel::class.java)) {
            return ListOfRecordsViewModel(recordDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}