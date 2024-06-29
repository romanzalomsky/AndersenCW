package com.zalomsky.rickandmorty.domain.usecase

import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {

    suspend operator fun invoke(id: Int): Character = characterRepository.getCharacterById(id)
}