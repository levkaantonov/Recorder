package levkaantonov.com.study.recorder.ui.fragments.list_of_records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import levkaantonov.com.study.recorder.data.db.RecordsRepository
import javax.inject.Inject

class ListOfRecordsViewModel @Inject constructor(
    recordsRepository: RecordsRepository
) : ViewModel() {
    val records = recordsRepository.getALl()

    class ListOfRecordsViewModelFactory @Inject constructor(
        private val recordsRepository: RecordsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == ListOfRecordsViewModel::class.java)
            return ListOfRecordsViewModel(recordsRepository) as T
        }
    }
}