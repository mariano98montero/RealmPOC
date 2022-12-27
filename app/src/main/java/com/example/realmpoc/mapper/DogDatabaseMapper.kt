package com.example.realmpoc.mapper

import com.example.realmpoc.entity.Dog
import com.example.realmpoc.entity.DogEntity

class DogDatabaseMapper {

    fun transform(dogEntity: DogEntity): Dog = dogEntity.run {
        Dog(
            name = dogEntity.name,
            age = dogEntity.age
        )
    }

    fun transformToDogEntity(dog: Dog) =
        DogEntity().apply {
            name = dog.name
            age = dog.age
        }

    fun transformToListOfDog(dogList: List<DogEntity>): List<Dog> {
        return dogList.map { transform(it) }
    }
}
