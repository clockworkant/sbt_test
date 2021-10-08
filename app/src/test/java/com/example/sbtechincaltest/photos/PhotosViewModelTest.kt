package com.example.sbtechincaltest.photos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.sbtechincaltest.photos.data.PhotoData
import com.example.sbtechincaltest.photos.data.PhotoRepo
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PhotosViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var sut: PhotosViewModel

    @Mock
    private lateinit var observer: Observer<in PhotosModel>

    @Mock
    private lateinit var photosRepo: PhotoRepo

    private val dummyPhotoData = listOf<PhotoData>(
        PhotoData("title 1", "http://google.com/1"),
        PhotoData("abc 2", "http://alphabet.com/2"),
        PhotoData("spongebob 3", "http://pineapple.com/3")
    )

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun refreshSuccessfully_showPhotos(){
        //Given
        sut = PhotosViewModel(photosRepo)
        sut.photosModel.observeForever(observer)
        whenever(photosRepo.getAll()).thenReturn(Single.just(dummyPhotoData))

        //When
        sut.refreshPhotos()

        //Then
        val model = sut.photosModel.value!!

        assertEquals(model.photos, dummyPhotoData)
        assertThat(model.errorMessage, nullValue())
        assertThat(model.showLoading, equalTo(false))
    }
}