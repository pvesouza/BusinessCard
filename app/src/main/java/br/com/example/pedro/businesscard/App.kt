package br.com.example.pedro.businesscard

import android.app.Application
import br.com.example.pedro.businesscard.data.AppDataBase
import br.com.example.pedro.businesscard.data.BusinessCardDao
import br.com.example.pedro.businesscard.data.BusinessCardRepository

class App : Application() {

    val dataBase by lazy{
        AppDataBase.getDataBase(this)
    }

    val repository by lazy {
        BusinessCardRepository(dataBase.businessDao())
    }
}