package uz.pdp.roundedtask.database.remotmediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import uz.pdp.roundedtask.database.AppDatabase
import uz.pdp.roundedtask.database.entity.CharEntity
import uz.pdp.roundedtask.database.entity.RemoteKeys
import uz.pdp.roundedtask.networking.ApiService
import uz.pdp.roundedtask.utils.STARTING_PAGE_INDEX
import uz.pdp.roundedtask.utils.convertChar
import java.io.IOException

@ExperimentalPagingApi
class CharEntityRemoteMediator  (
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, CharEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharEntity>
    ): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                if (appDatabase.charDao().count() > 0) return MediatorResult.Success(false)
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(true)
            }
            LoadType.APPEND -> {
                getKey()
            }
        }

        try {
            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX
            val apiResponse = apiService.getCharacters(page).body()
            val resultList = apiResponse?.results ?: emptyList()
            val charList = convertChar(resultList)
            val endOfPaginationReached = apiResponse?.info?.next == null
            appDatabase.withTransaction {
                val nextKey = page + 1
                appDatabase.remoteKeysDao().insertKey(
                    RemoteKeys(
                        0,
                        nextKey = nextKey,
                        isEndReached = endOfPaginationReached
                    )
                )

                appDatabase.charDao().insertChars(charList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): RemoteKeys? {
        return appDatabase.remoteKeysDao().getKeys().firstOrNull()
    }

}