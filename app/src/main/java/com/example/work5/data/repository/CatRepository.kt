package com.example.work5.data.repository

import com.example.work5.data.database.CatDao
import com.example.work5.data.model.Cat
import com.example.work5.data.network.CatApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CatRepository(private val catDao: CatDao, private val catApi: CatApi) {

    // Получение кота из Room
    suspend fun getCatFromDb(): Cat? = withContext(Dispatchers.IO) {
        catDao.getCat()
    }

    // Получение кота через API
    suspend fun fetchCatFromApi(): Response<List<Cat>> = withContext(Dispatchers.IO) {
        catApi.getCat().execute()
    }

    // Сохранение кота в базу данных
    suspend fun saveCatToDb(cat: Cat) = withContext(Dispatchers.IO) {
        catDao.insertCat(cat)
    }
}