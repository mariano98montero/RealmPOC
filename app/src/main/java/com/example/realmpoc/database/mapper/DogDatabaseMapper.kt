package com.example.realmpoc.database.mapper

import com.example.realmpoc.entity.Dog
import com.example.realmpoc.database.entity.DogEntity
import com.example.realmpoc.database.entity.OwnerEntity
import com.example.realmpoc.entity.Owner

class DogDatabaseMapper {

    fun transform(dogEntity: DogEntity): Dog = dogEntity.run {
        Dog(
            name = dogEntity.name,
            age = dogEntity.age,
            owner = dogEntity.owner?.let {
                Owner(
                    name = dogEntity.owner!!.name,
                    address = dogEntity.owner!!.address
                )
            }
        )
    }

    fun transformToDogEntity(dog: Dog) =
        DogEntity().apply {
            name = dog.name
            age = dog.age
            owner = OwnerEntity().apply {
                dog.owner?.let {
                    name = dog.owner!!.name
                    address = dog.owner!!.address
                }
            }
        }

    fun transformToListOfDog(dogList: List<DogEntity>): List<Dog> {
        return dogList.map { transform(it) }
    }
}
