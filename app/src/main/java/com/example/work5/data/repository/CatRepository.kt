package com.example.work5.data.repository

import com.example.work5.data.database.CatDao
import com.example.work5.data.model.Cat
import com.example.work5.data.network.CatApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatRepository(private val catDao: CatDao, private val catApi: CatApi) {

    fun fetchCatFromApi(callback: (Result<List<Cat>>) -> Unit) {
        catApi.getCat().enqueue(object : Callback<List<Cat>> {
            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if (response.isSuccessful) {
                    response.body()?.let { cats ->
                        callback(Result.success(cats))  // Успешный результат
                    } ?: run {
                        callback(Result.failure(Throwable("No cat data found")))
                    }
                } else {
                    callback(Result.failure(Throwable("Error code: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                callback(Result.failure(t))  // Обработка ошибки
            }
        })
    }

    // Сохранение кота в базу данных
    suspend fun saveCatToDb(cat: Cat) = withContext(Dispatchers.IO) {
        catDao.insertCat(cat)
    }

    // Получение кота из базы данных
    suspend fun getCatFromDb(): Cat? = withContext(Dispatchers.IO) {
        catDao.getCat()
    }
}

//    // Получение кота через API
//    suspend fun fetchCatFromApi(): Response<List<Cat>> = withContext(Dispatchers.IO) {
//    catApi.getCat().execute()
//    }












