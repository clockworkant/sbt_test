package com.example.sbtechincaltest.photos.data

import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit


interface DataSource {
    fun getPhotos(): Single<List<PhotoData>>
}

class LocalDataSource(cakeInputStream: InputStream) : DataSource {
    private val photos: List<PhotoData> =
        Gson().fromJson(InputStreamReader(cakeInputStream), Array<PhotoData>::class.java).toList()

    override fun getPhotos() = Single.just(photos)
        .delay(2, TimeUnit.SECONDS) //The delay is to emulate a web call (for progress spinners etc)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}

class RemoteDataSource : DataSource {
    private val service: PhotoService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        service = retrofit.create(PhotoService::class.java)
    }

    override fun getPhotos(): Single<List<PhotoData>> {
        return service.getPhotos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private interface PhotoService {
        @GET("/photos")
        fun getPhotos(): Single<List<PhotoData>>
    }
}