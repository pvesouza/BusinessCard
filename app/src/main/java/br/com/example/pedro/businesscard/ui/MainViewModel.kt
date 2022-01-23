package br.com.example.pedro.businesscard.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.example.pedro.businesscard.data.BusinessCard
import br.com.example.pedro.businesscard.data.BusinessCardRepository
import java.lang.IllegalArgumentException

class MainViewModel(
    private val businessCardRepository: BusinessCardRepository
) : ViewModel(){

    fun insert(businessCard: BusinessCard){
        businessCardRepository.insert(businessCard)
    }

    fun getAll() : LiveData<List<BusinessCard>> {
        return businessCardRepository.getAll()
    }
}

class MainViewModelFactory(private val businessCardRepository: BusinessCardRepository
    ) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(businessCardRepository) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}