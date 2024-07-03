package com.zalomsky.rickandmorty.features.characters.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.usecase.characters.GetCharacterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsCharacterViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
): ViewModel() {

    fun getCharacterById(id: Int): Flow<Character> {
        return flow {
            val character = getCharacterByIdUseCase(id)
            emit(character)
        }.catch { e ->
            Log.e("DetailsCharacterViewModel", "Error getting character: ${e.message}")
        }.flowOn(Dispatchers.IO)
    }
}