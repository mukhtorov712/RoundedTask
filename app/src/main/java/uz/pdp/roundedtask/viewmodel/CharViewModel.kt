package uz.pdp.roundedtask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import uz.pdp.roundedtask.database.entity.CharEntity
import uz.pdp.roundedtask.model.Result
import uz.pdp.roundedtask.networking.ApiService
import uz.pdp.roundedtask.repository.CharDataSource
import uz.pdp.roundedtask.repository.CharRepository
import javax.inject.Inject

@HiltViewModel
class CharViewModel @Inject constructor(private val repository: CharRepository) : ViewModel() {

    private var currentResult: Flow<PagingData<CharEntity>>? = null

    @ExperimentalPagingApi
    fun getCharEntity(): Flow<PagingData<CharEntity>> {
        val newResult: Flow<PagingData<CharEntity>> =
            repository.getCharEntity().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}