package uz.pdp.roundedtask.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.pdp.roundedtask.database.dao.CharDao
import uz.pdp.roundedtask.database.dao.RemoteKeysDao
import uz.pdp.roundedtask.database.entity.CharEntity
import uz.pdp.roundedtask.database.entity.RemoteKeys

@Database(entities = [CharEntity::class, RemoteKeys::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charDao(): CharDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}