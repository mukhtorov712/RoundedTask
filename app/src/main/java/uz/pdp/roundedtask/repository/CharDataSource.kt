package uz.pdp.roundedtask.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import uz.pdp.roundedtask.database.entity.CharEntity
import uz.pdp.roundedtask.networking.ApiService
import uz.pdp.roundedtask.utils.STARTING_PAGE_INDEX
import uz.pdp.roundedtask.utils.convertChar
import java.io.IOException

class CharDataSource(private val apiService: ApiService) :
    PagingSource<Int, CharEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharEntity> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = apiService.getCharacters(page).body()
            val resultList = response?.results ?: emptyList()
            val charList = convertChar(resultList)
            LoadResult.Page(
                data = charList,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response?.info?.next == null) null else page + 1
            )
        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }


    }

    override fun getRefreshKey(state: PagingState<Int, CharEntity>): Int? {
        return state.anchorPosition
    }
}