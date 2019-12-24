package com.mohamed.findnearbyapp.ViewModels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mohamed.findnearbyapp.Models.Item
import com.mohamed.findnearbyapp.Repositories.MainRepository


class MainViewModel(application: Application) : AndroidViewModel(application) {

    var repository : MainRepository = MainRepository(application.applicationContext)

    fun  getAllPlaces(lat : Double, lng : Double) : LiveData<List<Item>>?{
        return repository.getAllVenues(lat,lng)
    }
}