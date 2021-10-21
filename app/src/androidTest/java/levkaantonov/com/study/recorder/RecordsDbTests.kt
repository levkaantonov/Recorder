package levkaantonov.com.study.recorder

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertEquals
import levkaantonov.com.study.recorder.data.db.Record
import levkaantonov.com.study.recorder.data.db.RecordDao
import levkaantonov.com.study.recorder.data.db.RecordsDb
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecordsDbTests {
    private lateinit var recordDao: RecordDao
    private lateinit var db: RecordsDb

    @Before
    fun createDb() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, RecordsDb::class.java)
            .allowMainThreadQueries()
            .build()

        recordDao = db.recordDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testDb() {
        recordDao.create(Record())
        val count = recordDao.getCount()
        assertEquals(count.value, 1)
    }
}