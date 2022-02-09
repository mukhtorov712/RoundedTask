package uz.pdp.roundedtask.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "char_entity")
data class CharEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val url: String,
    val image: String,
    val location: String
)