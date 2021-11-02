package levkaantonov.com.study.recorder.data.db

import androidx.lifecycle.LiveData
import levkaantonov.com.study.recorder.data.db.LiveDataMutableListOf
import levkaantonov.com.study.recorder.data.db.Record
import levkaantonov.com.study.recorder.data.db.RecordDao
import levkaantonov.com.study.recorder.data.db.RecordsRepository
import javax.inject.Inject

class RecordsRepositoryImpl @Inject constructor(
    private val recordDao: RecordDao
) : RecordsRepository {

    override fun create(record: Record) = recordDao.create(record)

    override fun getRecord(id: Int): Record? = recordDao.getRecord(id)

    override fun getALl(): LiveDataMutableListOf<Record> = recordDao.getALl()

    override fun update(record: Record) = recordDao.update(record)

    override fun deleteAll() = recordDao.deleteAll()

    override fun deleteById(id: Int) = recordDao.deleteById(id)

    override fun getCount(): LiveData<Int> = recordDao.getCount()
}