package com.zalomsky.rickandmorty.domain.models.mappers

import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.CharacterResponse

fun CharacterEntity.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin,
        location = location,
        image = image,
        episode = episode,
        url = url,
        created = created
    )
}

fun CharacterResponse.toCharacterEntityList(): List<CharacterEntity> {
    return results.map { it.toCharacterEntity() }
}

class CharacterMapper {

    fun fromApiToEntity(apiCharacter: CharacterEntity): CharacterEntity {
        return apiCharacter
    }

    fun fromResponseToEntityList(response: CharacterResponse): List<CharacterEntity> {
        return response.results.map { fromApiToEntity(it) }
    }
}