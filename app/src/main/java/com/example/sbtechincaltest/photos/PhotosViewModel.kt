package com.example.sbtechincaltest.photos

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sbtechincaltest.photos.data.PhotoData
import com.example.sbtechincaltest.photos.data.PhotoRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PhotosViewModel(
    private val photoRepo: PhotoRepo
) : ViewModel() {

    private val _photosModel = MutableLiveData<PhotosModel>()
    val photosModel = _photosModel as LiveData<PhotosModel>

    private val disposable = CompositeDisposable()

    fun loadPhotos() {
        _photosModel.value = PhotosModel(showLoading = true)
        disposable.add(
            photoRepo.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ results ->
                    _photosModel.value = PhotosModel(photos = results)
                }, {
                    _photosModel.value =
                        PhotosModel(errorMessage = "Couldn't get photos, please try again")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}

data class PhotosModel(
    val photos: List<PhotoData>? = null,
    val errorMessage: String? = null,
    val showLoading: Boolean = false,
)