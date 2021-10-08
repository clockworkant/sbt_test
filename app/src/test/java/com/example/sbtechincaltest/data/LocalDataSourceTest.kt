package com.example.sbtechincaltest.data

import com.example.sbtechincaltest.photos.data.LocalDataSource
import com.example.sbtechincaltest.photos.data.PhotoData
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import java.io.BufferedInputStream
import java.io.FileInputStream

class LocalDataSourceTest {
    val photosPath = this.javaClass.getResource("/photos.json").path
    val localDataSource = LocalDataSource(BufferedInputStream(FileInputStream(photosPath)))

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `assert number of photos`() {

        localDataSource.getPhotos()
            .toObservable()
            .test()
            .assertValue {
                it.size == 5000
            }
    }

    /**
     * A really quick way to check the json deserialisation worked without spinning up an android vm
     */
    @Test
    fun `assert photos deseralised`() {
        val expectedPhoto = PhotoData(
            "accusamus beatae ad facilis cum similique qui sunt",
            "https://via.placeholder.com/150/92c952")
        localDataSource.getPhotos()
            .toObservable()
            .test()
            .assertValue {
                it.first() == expectedPhoto
            }
    }
}