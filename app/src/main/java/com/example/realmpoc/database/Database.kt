package com.example.realmpoc.database

import com.example.realmpoc.entity.Dog
import com.example.realmpoc.database.entity.DogEntity
import com.example.realmpoc.database.entity.OwnerEntity
import com.example.realmpoc.database.mapper.DogDatabaseMapper
import com.example.realmpoc.util.Result
import com.example.realmpoc.util.Utils
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmSingleQuery
import java.util.Arrays
import java.util.Arrays.asList

object Database {
    // To set up Realm we need an schema, a class implementing RealmObject.
    private val configuration = RealmConfiguration.Builder(schema = setOf(DogEntity::class, OwnerEntity::class))
        // Here we set Realm so if you change the Realm Object schema, it will take the new one.
        // However be careful because this will delete all data saved.
        .schemaVersion(1)
        .deleteRealmIfMigrationNeeded()
        .build()

    private val realm = Realm.open(configuration)

    private val mapper = DogDatabaseMapper()

    suspend fun saveDog(dog: Dog): Result<String> {
        val dogEntity: DogEntity = mapper.transformToDogEntity(dog)
        // With this query we are checking if there is a dog already with that name, this is important
        // because in our schema, the Dog name is also the primary key.
        if (realm.query<DogEntity>("name = $0", dog.name).find().isEmpty()) {
            // Use the reaml.write to write data on the database. This needs to be executed inside a suspend
            // function. You can use writeBlocking otherwise.
            realm.write { this.copyToRealm(dogEntity) }
            return Result.Success(Utils.SAVED_OK)
        } else {
            return Result.Failure(Exception(Utils.ALREADY_EXISTS))
        }
    }

    fun getDogs(): Result<List<Dog>> {
        // Here we get all dogs loaded in the database.
        val dogList = mapper.transformToListOfDog(realm.query<DogEntity>().find())
        if (dogList.isEmpty()) {
            return Result.Failure(Exception(Utils.NO_DOG_ADDED_YET))
        } else {
            return Result.Success(dogList)
        }
    }

    suspend fun deleteDogByName(name: String) {
        realm.write {
            // Use this query to get the dog with the name/primaryKey sended by parameter
            // so then we will be able to delete it from the database.
            val query: RealmSingleQuery<DogEntity> = query<DogEntity>("name = $0", name).first()
            delete(query)
        }
    }

    fun getDogsFiltered(ownerName: String): Result<List<Dog>> {
        return Result.Success(
            mapper.transformToListOfDog(realm.query<DogEntity>("owner.name = $0", ownerName).find())
        )
    }
}
