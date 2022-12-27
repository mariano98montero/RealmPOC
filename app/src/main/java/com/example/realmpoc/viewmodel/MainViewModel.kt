package com.example.realmpoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmpoc.database.Database
import com.example.realmpoc.entity.Dog
import com.example.realmpoc.util.Result
import com.example.realmpoc.util.Utils.EMPTY_STRING
import com.example.realmpoc.viewmodel.MainViewModel.MainStatus.DATA_FAILURE
import com.example.realmpoc.viewmodel.MainViewModel.MainStatus.DELETE_OK
import com.example.realmpoc.viewmodel.MainViewModel.MainStatus.HIDE_LOADER
import com.example.realmpoc.viewmodel.MainViewModel.MainStatus.SAVE_OK
import com.example.realmpoc.viewmodel.MainViewModel.MainStatus.SHOW_LOADER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private var liveData = MutableLiveData<MainData>()

    fun getLiveData(): LiveData<MainData> = liveData

    fun getData() = viewModelScope.launch {
        liveData.postValue(MainData(status = SHOW_LOADER))
        withContext(Dispatchers.IO) { Database.getDogs() }.let { result ->
            liveData.postValue(MainData(status = HIDE_LOADER))
            when (result) {
                is Result.Success -> liveData.postValue(
                    MainData(status = MainStatus.DATA_SUCCESS, dogList = result.data)
                )
                is Result.Failure -> liveData.postValue(
                    result.exception.message?.let { MainData(status = DATA_FAILURE, errorMessage = it) }
                )
            }
        }
    }

    fun deleteDog(dogName: String) = viewModelScope.launch {
        liveData.postValue(MainData(status = SHOW_LOADER))
        withContext(Dispatchers.IO) { Database.deleteDogByName(dogName) }.let {
            liveData.postValue(MainData(status = HIDE_LOADER))
            liveData.postValue(MainData(status = DELETE_OK))
        }
    }

    fun addDog(dog: Dog) = viewModelScope.launch {
        liveData.postValue(MainData(status = SHOW_LOADER))
        withContext(Dispatchers.IO) { Database.saveDog(dog) }.let { result ->
            liveData.postValue(MainData(status = HIDE_LOADER))
            when (result) {
                is Result.Success -> liveData.postValue(MainData(status = SAVE_OK))
                is Result.Failure -> liveData.postValue(
                    result.exception.message?.let { MainData(status = DATA_FAILURE, errorMessage = it) }
                )
            }
        }
    }

    data class MainData(
        val status: MainStatus,
        val dogList: List<Dog> = emptyList(),
        val errorMessage: String = EMPTY_STRING
    )

    enum class MainStatus {
        DATA_SUCCESS,
        DATA_FAILURE,
        SAVE_OK,
        DELETE_OK,
        SHOW_LOADER,
        HIDE_LOADER
    }
}
