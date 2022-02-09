package uz.pdp.roundedtask.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.pdp.roundedtask.database.entity.CharEntity

@Dao
interface CharDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChars(list: List<CharEntity>)

    @Query("SELECT * FROM char_entity")
    fun getCharList(): PagingSource<Int, CharEntity>

    @Query("DELETE FROM char_entity")
    suspend fun clearRepos()

    @Query("SELECT COUNT(id) from char_entity")
    suspend fun count(): Int
}