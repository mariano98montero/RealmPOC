package com.example.realmpoc.di

import com.example.realmpoc.database.Database
import com.example.realmpoc.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single<Database> { Database }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}
