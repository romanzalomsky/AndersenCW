package com.zalomsky.rickandmorty.domain.usecase.characters

import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class GetAllCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {

    suspend fun execute(
        page: Int,
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): List<CharacterEntity> {
        return characterRepository.getCharactersList(page, name, status, species, gender)
    }
}