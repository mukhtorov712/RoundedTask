package uz.pdp.roundedtask.utils

import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import uz.pdp.roundedtask.database.entity.CharEntity
import uz.pdp.roundedtask.model.Result


suspend fun convertChar(resultList:List<Result>): List<CharEntity> {
    val charList = ArrayList<CharEntity>()
    coroutineScope {
        withContext(Dispatchers.Default) {
            resultList.forEach {
                charList.add(
                    CharEntity(
                        id = it.id,
                        name = it.name,
                        status = it.status,
                        species = it.species,
                        url = it.url,
                        image = it.image,
                        location = it.location.name
                    )
                )
            }
        }
    }
    return charList
}

fun View.show(){
    this.visibility = View.VISIBLE
}
fun View.hide(){
    this.visibility = View.GONE
}