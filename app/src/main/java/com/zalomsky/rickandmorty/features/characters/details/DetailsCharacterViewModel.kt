package com.zalomsky.rickandmorty.features.characters.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.usecase.characters.GetCharacterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsCharacterViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
): ViewModel() {

    private val _characterById = MutableLiveData<Character>()
    val characterById: LiveData<Character>
        get() = _characterById

    fun getCharacterById(id: Int) {
        viewModelScope.launch {
            try {
                val character = getCharacterByIdUseCase(id)
                _characterById.postValue(character)
            } catch (e: Exception) {
                Log.e("asdfghjk", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }
}