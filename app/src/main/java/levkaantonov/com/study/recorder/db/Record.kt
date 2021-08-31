package levkaantonov.com.study.recorder.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    val name: String = "",
    @ColumnInfo
    val path: String = "",
    @ColumnInfo
    var length: Long = 0L,
    @ColumnInfo
    var timeOfCreation: Long = 0L
)