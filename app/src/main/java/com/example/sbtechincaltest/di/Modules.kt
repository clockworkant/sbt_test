package com.example.sbtechincaltest.di

import com.example.sbtechincaltest.login.LoginViewModel
import com.example.sbtechincaltest.photos.PhotosViewModel
import com.example.sbtechincaltest.photos.data.DataSource
import com.example.sbtechincaltest.photos.data.PhotoRepo
import com.example.sbtechincaltest.photos.data.PhotoRepoImpl
import com.example.sbtechincaltest.photos.data.RemoteDataSource
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repoModule = module {
    //TODO comment out the datasource you don't want to use. Allows for offine development.
//    single<DataSource> { LocalDataSource(androidContext().assets.open("photos.json")) }
    single<DataSource> { RemoteDataSource() }
    single<PhotoRepo> { PhotoRepoImpl(get()) }

    viewModel { PhotosViewModel(get()) }
    viewModel { LoginViewModel() }
}