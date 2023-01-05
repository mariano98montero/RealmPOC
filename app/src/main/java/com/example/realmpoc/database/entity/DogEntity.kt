package com.example.realmpoc.database.entity

import com.example.realmpoc.util.Utils.EMPTY_STRING
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class DogEntity: RealmObject {
    @PrimaryKey
    var name: String = EMPTY_STRING
    var age: Int = 0
    var owner: OwnerEntity? = null
}
