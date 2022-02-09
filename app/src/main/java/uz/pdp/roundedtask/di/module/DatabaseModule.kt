package uz.pdp.roundedtask.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.pdp.roundedtask.database.AppDatabase
import uz.pdp.roundedtask.database.dao.CharDao
import uz.pdp.roundedtask.database.dao.RemoteKeysDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideCharDao(appDatabase: AppDatabase): CharDao = appDatabase.charDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(appDatabase: AppDatabase): RemoteKeysDao = appDatabase.remoteKeysDao()
}