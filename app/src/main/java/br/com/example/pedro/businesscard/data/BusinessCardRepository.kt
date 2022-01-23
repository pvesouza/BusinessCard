package br.com.example.pedro.businesscard.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BusinessCardRepository(private val businessCardDao: BusinessCardDao) {

    fun insert(businessCard: BusinessCard) = runBlocking {
        launch(Dispatchers.IO){
            businessCardDao.insert(businessCard)
        }
    }

    fun getAll() = businessCardDao.getAll()
}