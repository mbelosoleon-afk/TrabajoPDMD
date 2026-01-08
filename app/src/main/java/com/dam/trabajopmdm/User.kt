import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(

    @PrimaryKey val uid: Int,
    @ColumnInfo("record") val record: String?,
    @ColumnInfo("fecha") val fecha: String?
)