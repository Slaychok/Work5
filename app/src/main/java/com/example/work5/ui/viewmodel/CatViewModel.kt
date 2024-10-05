package com.example.work5.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.work5.data.network.CatApi
import com.example.work5.data.database.CatDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatViewModel(application: Application) : AndroidViewModel(application) {

    private val catDao = CatDatabase.getDatabase(application).catDao()

    val catImageUrl = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val catApi: CatApi = retrofit.create(CatApi::class.java)

    fun fetchCat() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = catApi.getCat().execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isNotEmpty()) {
                            val cat = it[0]
                            catDao.insertCat(cat) // Сохранение кота в базу данных
                            catImageUrl.postValue(cat.url)
                        } else {
                            error.postValue("No cat data found")
                        }
                    }
                } else {
                    error.postValue("Error code: ${response.code()}")
                }
            } catch (e: Exception) {
                error.postValue(e.message)
            }
        }
    }

    fun loadCatFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val cat = catDao.getCat()
            if (cat != null) {
                catImageUrl.postValue(cat.url)
            } else {
                error.postValue("No cat in database")
            }
        }
    }
}
