package com.example.sbtechincaltest.photos.data

import io.reactivex.Single

interface PhotoRepo {
    fun getAll(): Single<List<PhotoData>>
}

class PhotoRepoImpl(private val dataSource: DataSource) : PhotoRepo {
    override fun getAll(): Single<List<PhotoData>> = dataSource.getPhotos()
}