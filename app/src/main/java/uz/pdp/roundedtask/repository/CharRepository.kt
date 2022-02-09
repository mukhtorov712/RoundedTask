package uz.pdp.roundedtask.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.pdp.roundedtask.database.AppDatabase
import uz.pdp.roundedtask.database.entity.CharEntity
import uz.pdp.roundedtask.database.remotmediator.CharEntityRemoteMediator
import uz.pdp.roundedtask.networking.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharRepository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) {
//    fun getCharacters(page: Int) = flow { emit(apiService.getCharacters(page)) }

    private val pagingSourceFactory = { appDatabase.charDao().getCharList() }

    @ExperimentalPagingApi
    fun getCharEntity(): Flow<PagingData<CharEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                enablePlaceholders = false
            ),
            remoteMediator = CharEntityRemoteMediator(apiService, appDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}