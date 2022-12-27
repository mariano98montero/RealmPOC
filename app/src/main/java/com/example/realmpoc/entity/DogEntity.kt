package com.example.realmpoc.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


open class DogEntity: RealmObject {
    @PrimaryKey
    var name: String = ""
    var age: Int = 0
}