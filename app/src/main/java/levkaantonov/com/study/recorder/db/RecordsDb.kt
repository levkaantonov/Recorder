package levkaantonov.com.study.recorder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1)
abstract class RecordsDb : RoomDatabase() {
    abstract val recordDao: RecordDao

    companion object {
        private const val DB_NAME = "records_database"

        @Volatile
        private var instance: RecordsDb? = null

        fun getInstance(context: Context): RecordsDb {
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    RecordsDb::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance = db
                return db
            }
        }
    }
}