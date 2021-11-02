package levkaantonov.com.study.recorder.data.db

import androidx.lifecycle.LiveData
import levkaantonov.com.study.recorder.data.db.LiveDataMutableListOf
import levkaantonov.com.study.recorder.data.db.Record

interface RecordsRepository {

    fun create(record: Record)

    fun getRecord(id: Int): Record?

    fun getALl(): LiveDataMutableListOf<Record>

    fun update(record: Record)

    fun deleteAll()

    fun deleteById(id: Int)

    fun getCount(): LiveData<Int>
}