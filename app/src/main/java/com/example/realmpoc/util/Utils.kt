package com.example.realmpoc.util

object Utils {
    const val SAVED_OK = "SAVED_OK"
    const val ALREADY_EXISTS = "ALREADY_EXISTS"
    const val NO_DOG_ADDED_YET = "NO_DOG_ADDED_YET"
    const val EMPTY_STRING = ""

    fun String.firstToUpperCase(): String =
        this.substring(0, 1).uppercase() + this.substring(1).lowercase()
}
