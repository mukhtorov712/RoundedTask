package uz.pdp.roundedtask.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.pdp.roundedtask.model.DataCharacter

interface ApiService {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<DataCharacter>



}