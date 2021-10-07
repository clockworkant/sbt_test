package com.example.sbtechincaltest.photos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sbtechincaltest.photos.data.PhotoData
import com.example.sbtechincaltest.photos.data.PhotoRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PhotosViewModel(
    private val photoRepo: PhotoRepo
) : ViewModel() {

    private val _photos = MutableLiveData<List<PhotoData>>()
    val photos = _photos as LiveData<List<PhotoData>>

    fun loadPhotos() {
        photoRepo.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ results ->
                _photos.value = results
            }, {
                Log.e("Alec", "photo fetch error", it)
            }
            )

    }

}