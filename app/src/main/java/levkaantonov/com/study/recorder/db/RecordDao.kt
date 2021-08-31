package levkaantonov.com.study.recorder.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

typealias LiveDataMutableListOf<T> = LiveData<MutableList<T>>

@Dao
interface RecordDao {
    @Insert
    fun create(record: Record)

    @Query("select * from records where id = :id")
    fun getRecord(id: Int): Record?

    @Query("select * from records")
    fun getALl(): LiveDataMutableListOf<Record>

    @Update
    fun update(record: Record)

    @Query("delete from records")
    fun deleteAll()

    @Query("delete from records where id = :id")
    fun deleteById(id: Int)

    @Query("select count(*) from records")
    fun getCount() : LiveData<Int>
}