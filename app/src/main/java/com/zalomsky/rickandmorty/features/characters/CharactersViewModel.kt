package com.zalomsky.rickandmorty.features.characters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.model.CharacterResponse
import com.zalomsky.rickandmorty.domain.usecase.GetAllCharactersUseCase
import com.zalomsky.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersUseCase: GetAllCharactersUseCase,
    private val characterUseCase: GetCharacterByIdUseCase
) : ViewModel() {

    private val _characters = MutableLiveData<CharacterResponse>()
    val characters: LiveData<CharacterResponse>
        get() = _characters

    private val _charactersById = MutableLiveData<Character>()
    val charactersById: LiveData<Character>
        get() = _charactersById

    fun getAllCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val characters = charactersUseCase()
                _characters.postValue(characters)
            } catch (e: Exception) {
                Log.e("asdfghjk", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }

    fun getCharacterById(id: Int) {
        viewModelScope.launch {
            try {
                val character = characterUseCase(id)
                _charactersById.postValue(character)
            } catch (e: Exception) {
                Log.e("asdfghjk", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }
}