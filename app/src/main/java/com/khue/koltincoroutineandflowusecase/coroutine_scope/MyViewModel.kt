package com.khue.koltincoroutineandflowusecase.coroutine_scope

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//https://medium.com/androiddevelopers/easy-coroutines-in-android-viewmodelscope-25bffb605471
//https://medium.com/google-developer-experts/viewmodels-under-the-hood-f8e286c4cc72
class MyViewModel: ViewModel() {

    init {
        viewModelScope.launch {
            // Coroutine code goes here
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}