package com.example.realmpoc.database.entity

import com.example.realmpoc.util.Utils.EMPTY_STRING
import io.realm.kotlin.types.RealmObject

open class OwnerEntity: RealmObject {
    var name: String = EMPTY_STRING
    var address: String = EMPTY_STRING
}
